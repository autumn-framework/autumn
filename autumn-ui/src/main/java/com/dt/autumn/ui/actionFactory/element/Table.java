package com.dt.autumn.ui.actionFactory.element;

import com.dt.autumn.reporting.assertions.CustomAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table extends UIElement {

	@Deprecated
	public Table(By by, String pageName) {
		super(by, pageName);
	}

	public Table(By by, String pageName, String elementName) {
		super(by, pageName, elementName);
	}

	public int getRowCount() {
		return getRows().size();
	}

	public int getColumnCount() {
		return getWrappedElement().findElements(By.cssSelector("th")).size();
	}

	public WebElement getCellAtIndex(int rowIdx, int colIdx) {
		WebElement row = getRows().get(rowIdx);
		List<WebElement> cells;
		// Cells are most likely to be td tags
		if ((cells = row.findElements(By.tagName("td"))).size() > 0) {
			return cells.get(colIdx - 1);
		}
		// Failing that try th tags
		else if ((cells = row.findElements(By.tagName("th"))).size() > 0) {
			return cells.get(colIdx - 1);
		} else {
			final String error = String.format("Could not find cell at row: %s column: %s", rowIdx, colIdx);
			throw new RuntimeException(error);
		}
	}

	public String getCellTextAtIndex(int rowIdx, int colIdx) {
		return getCellAtIndex(rowIdx, colIdx).getText();
	}

	private List<WebElement> getRows() {
		List<WebElement> rows = new ArrayList<WebElement>();
		// Header rows
		List<WebElement> headerRows = getWrappedElement().findElements(By.cssSelector("thead tr"));

		if (headerRows.size() > 0) {
			rows.add(headerRows.get(0));
		} else {
			rows.add(null);
		}
		// Body rows
		List<WebElement> bodyRows = getWrappedElement().findElements(By.cssSelector("tbody tr"));
		if (bodyRows.size() > 0) {
			rows.addAll(bodyRows);
		}
		// Footer rows
		/*
		 * List<WebElement> footerRows =
		 * getWrappedElement().findElements(By.cssSelector("tfoot tr"));
		 * rows.addAll(footerRows);
		 */

		return rows;
	}

	public Map<Object, Map<Object, Object>> getTableData(String differentiator) {
		Map<Object, Map<Object, Object>> finalMap = new HashMap<Object, Map<Object, Object>>();
		Map<Object, Object> tableData = new HashMap<Object, Object>();
		List<WebElement> headerRows = getWrappedElement().findElements(By.cssSelector("thead tr"));
		List<WebElement> bodyRows = getWrappedElement().findElements(By.cssSelector("tbody tr"));
		List<WebElement> header = new ArrayList<WebElement>();
		List<List<WebElement>> var = new ArrayList<List<WebElement>>();

		if (headerRows.size() > 0)
			header = headerRows.get(0).findElements(By.tagName("th"));
		if (bodyRows.size() > 0) {
			for (int i = 0; i < bodyRows.size(); i++) {
				var.add(bodyRows.get(i).findElements(By.tagName("td")));
			}
		}
		for (int j = 0; j < var.size(); j++) {
			tableData = new HashMap<Object, Object>();
			for (int i = 0; i < header.size(); i++) {
				tableData.put(header.get(i).getText(), var.get(j).get(i).getText());
			}
			for (Object headerKey : tableData.keySet()) {
				if (headerKey.equals(differentiator)) {
					finalMap.put(tableData.get(differentiator), tableData);
				}
			}
		}
		return finalMap;

	}

	public List<WebElement> getCells(WebElement row) {
		List<WebElement> cells;
		// Cells are most likely to be td tags
		if ((cells = row.findElements(By.tagName("td"))).size() > 0) {
			return cells;
		}
		// Failing that try th tags
		else if ((cells = row.findElements(By.tagName("th"))).size() > 0) {
			return cells;
		} else {
			final String error = String.format("Could not find any cell.");
			throw new RuntimeException(error);
		}
	}

	public List<String> getRowValue(int row) {
		List<WebElement> cellsElements = getCells(getRows().get(row));
		List<String> value = new ArrayList<>();
		for (WebElement ele : cellsElements)
			value.add(ele.getText());
		return value;
	}

	public int getColIdx(String colHeader) {
		try {
			for (int idx = 1;; idx++) {
				if (getCellTextAtIndex(0, idx).equalsIgnoreCase(colHeader)) {
					return idx;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("Column header not found");
		}

	}

	public void assertCellText(int rowIdx, int colIdx, String text) {
		CustomAssert.assertEqualsIgnoreCase(getCellAtIndex(rowIdx, colIdx).getText().trim(),text,"Assert [" + getElementName() + "] cell[" + rowIdx + "][" + colIdx + "] text equals ["
				+ text + "on [" + getPageName() + "]");
	}

	public void assertCellText(int rowIdx, String colHeader, String text) {
		CustomAssert.assertEqualsIgnoreCase(getCellAtIndex(rowIdx, getColIdx(colHeader)).getText().trim(),text,"Assert [" + getElementName() + "] cell[" + rowIdx + "][" + colHeader + "] text equals ["
				+ text + "on [" + getPageName() + "]");
	}

	public void assertCellContainsText(int rowIdx, int colIdx, String text) {
		CustomAssert.assertEqualsIgnoreCase(getCellAtIndex(rowIdx, colIdx).getText().trim(),text,"Assert [" + getElementName() + "] cell[" + rowIdx + "][" + colIdx + "] contains text ["
				+ text + "on [" + getPageName() + "]");
	}

	public void assertCellContainsText(int rowIdx, String colHeader, String text) {
		CustomAssert.assertEqualsIgnoreCase(getCellAtIndex(rowIdx, getColIdx(colHeader)).getText().trim(),text,"Assert [" + getElementName() + "] cell[" + rowIdx + "][" + colHeader
				+ "] contains text [" + text + "on [" + getPageName() + "]");
	}

	public void assertCellDoesNotContainText(int rowIdx, int colIdx, String text) {
		CustomAssert.assertNotContains(getCellAtIndex(rowIdx, colIdx).getText().trim(),text,"Assert [" + getElementName() + "] cell[" + rowIdx + "][" + colIdx
				+ "] doesn't contain text [" + text + "on [" + getPageName() + "]");
	}

	public void assertCellDoesNotContainText(int rowIdx, String colHeader, String text) {
		CustomAssert.assertNotContains(getCellAtIndex(rowIdx, getColIdx(colHeader)).getText().trim(),text,"Assert [" + getElementName() + "] cell[" + rowIdx + "][" + colHeader
				+ "] doesn't contain text [" + text + "on [" + getPageName() + "]");
	}

	private boolean isSortIconDisplayed(int colIdx) {
		try {
			return getCellAtIndex(0, colIdx).findElement(By.xpath(".//span[contains(@class, 'DataTables_sort_icon')]"))
					.isDisplayed();
		} catch (NoSuchElementException e) {
			// do nothing
		}
		return false;
	}

	public void assertColumnSortable(int colIdx) {
		CustomAssert.assertTrue(isSortIconDisplayed(colIdx),"Assert column " + colIdx + " is sortable on [" + getPageName());
	}

	public void assertColumnSortable(String colHeader) {
		CustomAssert.assertTrue(isSortIconDisplayed(getColIdx(colHeader)),"Assert column " + colHeader + " is sortable on [" + getPageName());
	}

	public void assertColumnNotSortable(int colIdx) {
		CustomAssert.assertFalse(isSortIconDisplayed(colIdx),"Assert column " + colIdx + " is not sortable on [" + getPageName());
	}

	public void assertColumnNotSortable(String colHeader) {
		CustomAssert.assertFalse(isSortIconDisplayed(getColIdx(colHeader)),"Assert column " + colHeader + " is sortable on [" + getPageName());
	}

}