package passport.infra.json;

import java.io.File;

public class JsonFile extends File {
    public JsonFile(String dir, String name) {
        super(dir + "/" + name + ".json");
        assert !dir.endsWith("/");
        assert !name.endsWith(".json");
    }
}
