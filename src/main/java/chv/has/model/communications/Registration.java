package chv.has.model.communications;

/**
 * @author Christopher Anciaux
 */
public class Registration {
    protected String name;

    public Registration(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
