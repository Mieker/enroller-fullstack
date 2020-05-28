package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;
    Session session;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
        session = connector.getSession();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public Meeting findById(long id) {
        return (Meeting) session.get(Meeting.class, id);
    }

    public Meeting add(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.save(meeting);
        transaction.commit();
        return meeting;
    }
    
    public void delete(Meeting meeting) {
        Transaction transaction = this.session.beginTransaction();
        session.delete(meeting);
        transaction.commit();
    }
    
    public void enlistParticipant(Participant participant, long id) {
        Transaction transaction = this.session.beginTransaction();
        Meeting meeting = (Meeting) session.get(Meeting.class, id);
        meeting.addParticipant(participant);
        transaction.commit();
    }

    public Participant findParticipantByLogin(String login) {
        return (Participant) session.get(Participant.class, login);
    }

    public boolean containParticipant(String givenlogin, Meeting meeting) {
        Collection<Participant> listOfparticipants = meeting.getParticipants();
        String login;
        for (Participant p : listOfparticipants) {
            login = p.getLogin();
            if (givenlogin.equals(login)) {
                return true;
            }
        }
        return false;
    }
    
    public void deleteParticipantFromMeeting(Meeting meeting, String login) {
        Collection<Participant> listOfParticipants = meeting.getParticipants();
        Iterator<Participant> i = listOfParticipants.iterator();
        while (i.hasNext()) {
            Participant p = i.next();
            if (p.getLogin().equals(login)) {
                Transaction transaction = this.session.beginTransaction();
                i.remove();
                session.save(meeting);
                transaction.commit();
            }
        }
    }
}
