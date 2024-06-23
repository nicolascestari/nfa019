package org.calendrier.model;

import javax.persistence.*;

@Entity
@Table(name = "activite")
public class Activite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "commentaire", nullable = false)
    private String commentaire;

    @Column(name = "statut", nullable = false)
    private String statut;

    public Activite() {}

    public Activite(String date, String commentaire, String statut) {
        this.date = date;
        this.commentaire = commentaire;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getStatut() {
        return statut;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
