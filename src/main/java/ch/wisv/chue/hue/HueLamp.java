package ch.wisv.chue.hue;

public class HueLamp {
    private String id;
    private String name;

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
}
