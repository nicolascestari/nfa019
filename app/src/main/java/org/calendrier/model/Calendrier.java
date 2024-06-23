package org.calendrier.model;

import java.util.ArrayList;
import java.util.List;

public class Calendrier {
    private List<Activite> activites;

    public Calendrier() {
        activites = new ArrayList<>();
    }

    public void ajouterActivite(Activite activite) {
        activites.add(activite);
    }

    // Other methods
}
