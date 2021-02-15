package com.example.setourn.Organizer;

import java.util.List;

public class LiveTournaments {

    String TournamentType,TournamentParticipants,OrganizerUsername,TournamentGame,Date,Time,OpenTill,TournamentID,Status,Winner;
    Long TimeMilli;
    List<String> participantsName;

    public LiveTournaments(){}

    public LiveTournaments(String tournamentType, String tournamentParticipants, String organizerUsername, String tournamentGame, String date, String time, String openTill, String tournamentID, String status, String winner, Long timeMilli, List<String> participantsName) {
        TournamentType = tournamentType;
        TournamentParticipants = tournamentParticipants;
        OrganizerUsername = organizerUsername;
        TournamentGame = tournamentGame;
        Date = date;
        Time = time;
        OpenTill = openTill;
        TournamentID = tournamentID;
        Status = status;
        Winner = winner;
        TimeMilli = timeMilli;
        this.participantsName = participantsName;
    }

    public String getTournamentType() {
        return TournamentType;
    }

    public void setTournamentType(String tournamentType) {
        TournamentType = tournamentType;
    }

    public String getTournamentParticipants() {
        return TournamentParticipants;
    }

    public void setTournamentParticipants(String tournamentParticipants) { TournamentParticipants = tournamentParticipants; }

    public String getOrganizerUsername() {
        return OrganizerUsername;
    }

    public void setOrganizerUsername(String organizerUsername) { OrganizerUsername = organizerUsername; }

    public String getTournamentGame() {
        return TournamentGame;
    }

    public void setTournamentGame(String tournamentGame) {
        TournamentGame = tournamentGame;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getOpenTill() {
        return OpenTill;
    }

    public void setOpenTill(String openTill) {
        OpenTill = openTill;
    }

    public Long getTimeMilli() {
        return TimeMilli;
    }

    public void setTimeMilli(Long timeMilli) {
        TimeMilli = timeMilli;
    }

    public String getTournamentID() {
        return TournamentID;
    }

    public void setTournamentID(String tournamentID) { TournamentID = tournamentID; }

    public String getStatus() { return Status; }

    public void setStatus(String status) { Status = status; }

    public List<String> getParticipantsName() {
        return participantsName;
    }

    public void setParticipantsName(List<String> participantsName) {
        this.participantsName = participantsName;
    }

    public String getWinner() {
        return Winner;
    }

    public void setWinner(String winner) {
        Winner = winner;
    }
}
