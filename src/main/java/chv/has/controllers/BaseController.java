package chv.has.controllers;

import chv.has.HAS;

/**
 * @author Christopher Anciaux
 */
abstract public class BaseController {
    private HAS HAS;

    protected HAS getHAS() {
        return this.HAS;
    }

    public void setHAS(HAS HAS) {
        this.HAS = HAS;
    }
}
