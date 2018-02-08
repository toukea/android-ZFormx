package istat.android.freedev.forms;

/**
 * Created by istat on 07/02/18.
 */

public final class ZFormx {

    public static FormFiller newFiller(Form form) {
        return FormFiller.using(form);
    }

    public static FormFiller newFiller(Form form, Class<?> cLass) {
        return FormFiller.using(form, cLass);
    }

    public static FormFiller newFiller(Class<?> cLassModel) {
        return FormFiller.usingModel(cLassModel);
    }

    public static FormFiller newFiller() {
        return FormFiller.usingNewForm();
    }

    public static FormFlower newFlower(Form form) {
        return FormFlower.using(form);
    }
}
