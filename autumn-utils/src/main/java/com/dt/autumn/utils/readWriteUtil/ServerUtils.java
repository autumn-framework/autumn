package com.dt.autumn.utils.readWriteUtil;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.dt.autumn.reporting.extentReport.Logger;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ServerUtils {

    private static Map<String, Session> sshMap = new HashMap<>();


    /**
     * Create the session with the other server and return the session Instance
     *
     * @param serverProperty
     * @return
     */
    public Session getSession(String serverProperty){
        String[] schemeSplit = serverProperty.split(":");
        String host = schemeSplit[0];
        String user = schemeSplit[1];
        String password = schemeSplit[2];

        try {
            Session session1 = null;
            if (sshMap.containsKey(host + ":" + user)) {
                session1 = sshMap.get(host + ":" + user);
                if (session1.isConnected()) {
                    return session1;
                } else
                    sshMap.remove(host + ":" + user);
            }
            JSch jschObj = new JSch();
            session1 = jschObj.getSession(user, host);
            session1.setPassword(password);
            session1.setConfig("StrictHostKeyChecking", "no");
            session1.connect();
            sshMap.put(host + ":" + user, session1);
            return session1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Disconnect the session
     *
     * @param session
     */
    public void disconnectSession(Session session){
        session.disconnect();
    }


    /**
     * Execute single shell command
     *
     * @param serverProperty
     * @param cmd
     */
    public void executeShellScript(String serverProperty,String cmd){
        try {
            Session session = getSession(serverProperty);
            Logger.logInfoInLogger("Connected session " + session.isConnected());
            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(cmd);
            BufferedReader reader=new BufferedReader(new InputStreamReader(channel.getInputStream()));
            channel.connect();
            Logger.logInfoInLogger("channel connections is " + channel.isConnected());
            channel.disconnect();
            disconnectSession(session);
        }catch (JSchException e){
            e.printStackTrace();
        }catch(IOException ie){
            ie.printStackTrace();
        }
    }

}
