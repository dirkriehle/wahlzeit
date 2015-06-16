package org.wahlzeit.model;

import org.wahlzeit.services.LogBuilder;
import org.wahlzeit.services.ObjectManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Abstract super class for UserManager. Contains all members and methods that can be offered for all Clients.
 * <p/>
 * Created by Lukas Hahmann on 29.05.15.
 */
public abstract class ClientManager extends ObjectManager {

    private static final Logger log = Logger.getLogger(ClientManager.class.getName());

    /**
     *
     */
    protected static Long lastClientId = 0L;

    /**
     * Maps IDs to user
     */
    protected Map<String, Client> idClientMap = new HashMap<String, Client>();

    protected HashMap<String, Client> httpSessionIdToClientMap = new HashMap<String, Client>();

    protected List<String> listOfUsedNicknames = new ArrayList<String>();


    // add methods -----------------------------------------------------------------------------------------------------

    /**
     * @methodtype set
     * @methodproperty wrapper
     */
    public void addClient(Client client) throws IllegalArgumentException {
        assertIsNonNullArgument(client);
        assertIsUnknownClientAsIllegalArgument(client);
        assertNicknameIsNotUsed(client.getNickName());

        doAddClient(client);
    }

    /**
     * @methodtype assertion
     */
    protected void assertIsUnknownClientAsIllegalArgument(Client client) {
        if (hasClientById(client.getId())) {
            throw new IllegalArgumentException(client.getId() + "is already known");
        }
    }

    /**
     * @methodtype assertion
     */
    protected void assertNicknameIsNotUsed(String nickName) {
        if (listOfUsedNicknames.contains(nickName)) {
            throw new IllegalArgumentException("Nickname " + nickName + " is already used.");
        }
    }

    /**
     * @methodtype set
     * @methodproperty primitive
     */
    protected void doAddClient(Client client) {
        idClientMap.put(client.getId(), client);
        writeObject(client);
        listOfUsedNicknames.add(client.getNickName());
        log.config(LogBuilder.createSystemMessage().addParameter("Added new user", client.getId()).toString());
    }

    /**
     * @methodtype boolean query
     */
    public boolean hasClientById(String id) {
        assertIsNonNullArgument(id, "user by Id");
        return getClientById(id) != null;
    }

    /**
     * @methodtype get
     * @methodproperty wrapper
     */
    public Client getClientById(String name) {
        assertIsNonNullArgument(name, "user name");

        Client result = doGetClientById(name);

        return result;
    }


    // get client methods ----------------------------------------------------------------------------------------------

    /**
     * @methodtype get
     * @methodproperty primitive
     */
    protected Client doGetClientById(String name) {
        return idClientMap.get(name);
    }

    /**
     * @methodtype set
     */
    public void addHttpSessionIdToClientMapping(String httpSessionId, Client client) {
        assertIsNonNullArgument(httpSessionId);
        assertIsNonNullArgument(client);
        assert !httpSessionIdToClientMap.containsKey(httpSessionId);

        doAddHttpSessionIdToClientMapping(httpSessionId, client);

        saveClient(client);
    }

    /**
     * @methodtype set
     */
    public void doAddHttpSessionIdToClientMapping(String httpSessionId, Client client) {
        httpSessionIdToClientMap.put(httpSessionId, client);
        client.setHttpSessionId(httpSessionId);
        log.config(LogBuilder.createSystemMessage().
                addParameter("client name", client.getNickName()).
                addParameter("httpSessionId", httpSessionId).toString());
    }


    // has client method -----------------------------------------------------------------------------------------------

    /**
     * @methodtype command
     */
    public void saveClient(Client client) {
        updateObject(client);
    }


    // save methods ----------------------------------------------------------------------------------------------------

    /**
     * @methodtype get
     */
    public Client getClientByHttpSessionId(String httpSessionId) {
        assertIsNonNullArgument(httpSessionId);

        return httpSessionIdToClientMap.get(httpSessionId);
    }

    /**
     * @methodtype command
     */
    public void saveClients() {
        updateObjects(idClientMap.values());
    }


    // client ID methods -----------------------------------------------------------------------------------------------

    /**
     * @methodtype get
     */
    public Long getLastClientId() {
        return lastClientId;
    }

    /**
     * @methodtype set
     */
    public synchronized void setLastClientId(Long newId) {
        lastClientId = newId;
    }

    /**
     * @methodtype get
     */
    public synchronized Long getNextClientId() {
        return ++lastClientId;
    }


    // delete methods --------------------------------------------------------------------------------------------------

    /**
     * @methodtype set
     */
    public void removeClient(Client client) {
        saveClient(client);
        idClientMap.remove(client.getId());
    }

    /**
     * @methodtype set
     * @methodproperty wrapper
     */
    public void deleteClient(Client client) {
        assertIsNonNullArgument(client);
        assert idClientMap.containsValue(client);

        removeHttpSessionIdToClientMapping(client.getHttpSessionId());
        doDeleteClient(client);

        assertIsUnknownUserAsIllegalState(client);
    }

    /**
     * @methodtype set
     */
    private void removeHttpSessionIdToClientMapping(String httpSessionId) {
        Client client = httpSessionIdToClientMap.get(httpSessionId);
        client.removeHttpSessionId();

        httpSessionIdToClientMap.remove(httpSessionId);
    }

    /**
     * @methodtype set
     * @methodproperty primtive
     */
    protected void doDeleteClient(Client client) {
        idClientMap.remove(client.getId());
        deleteObject(client);
    }

    /**
     * @methodtype assertion
     */
    protected void assertIsUnknownUserAsIllegalState(Client client) {
        if (hasClientById(client.getId())) {
            throw new IllegalStateException(client.getId() + "should not be known");
        }
    }

    // update methods --------------------------------------------------------------------------------------------------

    /**
     * @methodtype set
     */
    public void changeNickname(String oldNickName, String newNickName) throws IllegalArgumentException {
        assertNicknameIsNotUsed(newNickName);

        listOfUsedNicknames.remove(oldNickName);
        listOfUsedNicknames.add(newNickName);
    }
}
