package chv.has.utils.UserInterface;

import chv.has.HAS;

/**
 * @author Christopher Anciaux
 */
abstract public class AbstractUserInterfaceManager {
    protected HAS has;

    AbstractUserInterfaceManager(HAS has) {
        this.setHas(has);
    }

    public HAS getHas() {
        return this.has;
    }

    protected void setHas(HAS has) {
        this.has = has;
    }
}
