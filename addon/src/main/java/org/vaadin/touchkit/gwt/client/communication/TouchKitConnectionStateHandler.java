package org.vaadin.touchkit.gwt.client.communication;

import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.BAD_RESPONSE;
import static org.vaadin.touchkit.gwt.client.offlinemode.OfflineMode.ActivationReason.SERVER_AVAILABLE;

import org.vaadin.touchkit.gwt.client.offlinemode.OfflineModeEntrypoint;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.vaadin.client.communication.ConnectionStateHandler;
import com.vaadin.client.communication.DefaultConnectionStateHandler;
import com.vaadin.client.communication.PushConnection;
import com.vaadin.client.communication.XhrConnectionError;

import elemental.json.JsonObject;

/**
 * Custom {@link ConnectionStateHandler} for TouchKit.
 * 
 * If offline is not enabled it behaves like
 * {@link DefaultConnectionStateHandler}
 * 
 * Otherwise it dispatches the correct activation or deactivation reason for
 * offline mode
 */
public class TouchKitConnectionStateHandler extends
        DefaultConnectionStateHandler {

    private OfflineModeEntrypoint offlineModeEntrypoint = OfflineModeEntrypoint
            .get();

    @Override
    public void heartbeatException(Request request, Throwable exception) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.heartbeatException(request, exception);
        } else {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
        }
    }

    @Override
    public void heartbeatInvalidStatusCode(Request request, Response response) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.heartbeatInvalidStatusCode(request, response);
        } else {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
        }
    }

    @Override
    public void heartbeatOk() {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.heartbeatOk();
        } else {
            offlineModeEntrypoint.dispatch(SERVER_AVAILABLE);
        }
    }

    @Override
    public void pushClosed(PushConnection pushConnection,
            JavaScriptObject response) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.pushClosed(pushConnection, response);
        } else if (pushConnection.isBidirectional()) {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
        } else {
            // don't care about long polling as xhr is used for client -> server
        }
    }

    @Override
    public void pushClientTimeout(PushConnection pushConnection,
            JavaScriptObject response) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.pushClientTimeout(pushConnection, response);
        } else if (pushConnection.isBidirectional()) {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
        } else {
            // don't care about long polling as xhr is used for client -> server
        }
    }

    @Override
    public void pushError(PushConnection pushConnection,
            JavaScriptObject response) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.pushError(pushConnection, response);
        } else if (pushConnection.isBidirectional()) {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
        } else {
            // don't care about long polling as xhr is used for client -> server
        }

    }

    @Override
    public void pushReconnectPending(PushConnection pushConnection) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.pushReconnectPending(pushConnection);
        } else if (pushConnection.isBidirectional()) {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
        } else {
            // don't care about long polling as xhr is used for client -> server
        }
    }

    @Override
    public void pushOk(PushConnection pushConnection) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.pushOk(pushConnection);
        } else if (pushConnection.isBidirectional()) {
            OfflineModeEntrypoint.get().dispatch(SERVER_AVAILABLE);
        } else {
            // don't care about long polling as xhr is used for client -> server
        }
    }

    @Override
    public void xhrException(XhrConnectionError xhrConnectionError) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.xhrException(xhrConnectionError);
        } else {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
            getConnection().getMessageSender().endRequest();
        }
    }

    @Override
    public void xhrInvalidContent(XhrConnectionError xhrConnectionError) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.xhrInvalidContent(xhrConnectionError);
        } else {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
            getConnection().getMessageSender().endRequest();
        }
    }

    @Override
    public void xhrInvalidStatusCode(XhrConnectionError xhrConnectionError) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.xhrInvalidStatusCode(xhrConnectionError);
        } else {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
            getConnection().getMessageSender().endRequest();
        }
    }

    @Override
    public void xhrOk() {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.xhrOk();
        } else {
            offlineModeEntrypoint.dispatch(SERVER_AVAILABLE);
        }
    }

    @Override
    public void pushNotConnected(JsonObject payload) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.pushNotConnected(payload);
        } else {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
        }
    }

    @Override
    public void pushInvalidContent(PushConnection pushConnection, String message) {
        if (!offlineModeEntrypoint.isOfflineModeEnabled()) {
            super.pushInvalidContent(pushConnection, message);
        } else if (pushConnection.isBidirectional()) {
            offlineModeEntrypoint.dispatch(BAD_RESPONSE);
            getConnection().getMessageSender().endRequest();
        } else {
            // don't care about long polling as xhr is used for client -> server
        }
    }
}
