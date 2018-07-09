package cs.com.services;

import cs.com.entity.AlertEvent;
import cs.com.entity.JsonError;
import org.hibernate.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;

public class HsqlServiceImplTest {

    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    private HsqlService service;
    private static final String JSON = "{\"id\":\"scsmbstgrc\", \"state\":\"FINISHED\"*, \"timestamp\":1491377495218}";
    private static final String CAUSE = "Wrong character \"*\"";
    private static final String ID = "scsmbstgrc";
    private static final String TYPE = "APPLICATION_LOG";
    private static final String HOST = "test_host";
    private static final long ROW_COUNT = 2L;
    private static final long LONG_DURATION = 5L;
    private static final boolean IS_ALERT = true;
    private static final boolean IS_JSON_ERROR = true;

    @Before
    public void setUp() throws Exception {
        sessionFactory =  Mockito.mock(SessionFactory.class);
        session = Mockito.mock(Session.class);
        transaction = Mockito.mock(Transaction.class);
        service = new HsqlServiceImpl(sessionFactory);
        Mockito.when(sessionFactory.openSession()).thenReturn(session);
        Mockito.when(session.beginTransaction()).thenReturn(transaction);
        Mockito.when(session.getTransaction()).thenReturn(transaction);
    }

    @Test
    public void verifyIsAlertInDB() {
        Query query = Mockito.mock(Query.class);
        Mockito.when(session.createQuery(any(String.class))).thenReturn(query);
        AlertEvent alertEvent = getAlertEvent();
        Mockito.when(query.uniqueResult()).thenReturn(alertEvent);
        boolean isAlert = service.isAlertInDB(ID);
        Assert.assertEquals(IS_ALERT, isAlert);
    }

    @Test
    public void verifyIsJsonErrorInDB() {
        Query query = Mockito.mock(Query.class);
        Mockito.when(session.createQuery(any(String.class))).thenReturn(query);
        JsonError jsonError = getJsonError();
        Mockito.when(query.uniqueResult()).thenReturn(jsonError);
        boolean isJSonError = service.isJsonErrorInDB(JSON);
        Assert.assertEquals(IS_JSON_ERROR, isJSonError);
    }

    @Test
    public void verifySaveJsonError() {
        ArgumentCaptor<JsonError> argumentCaptor = ArgumentCaptor.forClass(JsonError.class);
        service.saveJsonError(JSON, CAUSE);
        Mockito.verify(session).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getJson(), equalTo(JSON));
        assertThat(argumentCaptor.getValue().getCause(), equalTo(CAUSE));
    }

    @Test
    public void verifySaveAlertEvent() {
        ArgumentCaptor<AlertEvent> argumentCaptor = ArgumentCaptor.forClass(AlertEvent.class);
        service.saveAlertEvent(ID, TYPE, HOST, LONG_DURATION, IS_ALERT);
        Mockito.verify(session).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId(), equalTo(ID));
        assertThat(argumentCaptor.getValue().getType(), equalTo(TYPE));
        assertThat(argumentCaptor.getValue().getHost(), equalTo(HOST));
        assertThat(argumentCaptor.getValue().getDuration(), equalTo(LONG_DURATION));
        assertThat(argumentCaptor.getValue().isAlert(), equalTo(IS_ALERT));
    }

    @Test
    public void verifyRemoveAlertEvent() {
        AlertEvent alertEvent = getAlertEvent();
        ArgumentCaptor<AlertEvent> argumentCaptor = ArgumentCaptor.forClass(AlertEvent.class);
        service.removeAlertEvent(alertEvent);
        Mockito.verify(session).delete(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), equalTo(alertEvent));
    }

    @Test
    public void retrunAlertEventsRowCount() {
        Criteria criteria = Mockito.mock(Criteria.class);
        List list = Mockito.mock(List.class);
        Mockito.when(session.createCriteria(AlertEvent.class)).thenReturn(criteria);
        Mockito.when(criteria.list()).thenReturn(list);
        Mockito.when(list.get(0)).thenReturn(ROW_COUNT);
        long rowCount = service.getAlertEventsRowCount();
        Assert.assertEquals(ROW_COUNT, rowCount);
    }

    private AlertEvent getAlertEvent() {
        AlertEvent alertEvent = new AlertEvent();
        alertEvent.setId(ID);
        alertEvent.setAlert(IS_ALERT);
        alertEvent.setType(TYPE);
        alertEvent.setHost(HOST);
        return alertEvent;
    }

    private JsonError getJsonError() {
        JsonError jsonError = new JsonError();
        jsonError.setJson(JSON);
        jsonError.setCause(CAUSE);
        return jsonError;
    }
}


