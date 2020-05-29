<template>
<div>
	<new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

	<span v-if="meetings.length == 0"> Brak zaplanowanych spotkań. </span>
	<h3 v-else>Zaplanowane zajęcia ({{ meetings.length }})</h3>

	<meetings-list :meetings="meetings" :username="username" :participant="participant"
		@attend="addMeetingParticipant($event)"
		@unattend="removeMeetingParticipant($event)"
		@delete="deleteMeeting($event)"></meetings-list>
</div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";

    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: [],
                participant: { "login": this.username },
             };
        },
        mounted() {
        	this.loadMeetings();
        },
        
        methods: {
            addNewMeeting(meeting) {
                this.meetings.push(meeting);
            },
            addMeetingParticipant(meeting) {
            	var url = "meetings/" + meeting.id + "/participants";
            	/* this.participant.login = this.username; */
            	 this.$http.post(url, this.participant)
                 .then(response => {
                	meeting.participants.push(response.body);
                	loadMeetings(); //check later if you can remove this line without errors
                     // udało się
                 })
                 .catch(response => {
                     // nie udało sie     
                 });
            },
            removeMeetingParticipant(meeting) {
            	
            	var pos = meeting.participants.map(function(e) { return e.login; }).indexOf(this.username);
            	
            	var url = "meetings/" + meeting.id + "/participants/" + this.username;
                this.$http.delete(url)
                .then(response => {
                	
                	meeting.participants.splice(pos, 1);
                	// udalo sie
                })
                .catch(response => {
                	// nie udalo sie
                });
            },
            deleteMeeting(meeting) {
            	var url = "meetings/" + meeting.id;
            	this.$http.delete(url)
                .then(response => {
                this.meetings.splice(this.meetings.indexOf(meeting), 1);
                    // udało się
                })
                .catch(response => {
                    // nie udało sie     
                });
            },
            loadMeetings() {
            	this.$http.get('meetings').then(response => {
            	    this.meetings = response.body;
				}, response => {
            	    // error callback
           		});
            },
        }
    }
</script>
