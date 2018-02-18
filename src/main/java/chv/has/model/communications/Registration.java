package chv.has.model.communications;

/**
 * @author Christopher Anciaux
 */
public class Registration {
    private String name;

    public Registration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
