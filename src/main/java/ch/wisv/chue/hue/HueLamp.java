package ch.wisv.chue.hue;

import java.util.Optional;

public class HueLamp implements Comparable<HueLamp> {
    private String id;
    private String name;
    private HueLightState lastState;

    public HueLamp(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<HueLightState> getLastState() {
        return Optional.ofNullable(lastState);
    }

    public void setLastState(HueLightState lastState) {
        this.lastState = lastState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HueLamp hueLamp = (HueLamp) o;

        return id.equals(hueLamp.id) && !(name != null ? !name.equals(hueLamp.name) : hueLamp.name != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(HueLamp o) {
        return id.compareTo(o.getId());
    }
}
