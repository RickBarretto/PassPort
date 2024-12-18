package passport.domain.models.events;

public enum EventCategory {
    CINE, CONFERENCE, FESTIVAL, MUSIC, NOT_PROVIDED, SPORTS, THEATRE, WORKSHOP;

    @Override
    public String toString() { return "category." + this.name().toLowerCase(); }
}
