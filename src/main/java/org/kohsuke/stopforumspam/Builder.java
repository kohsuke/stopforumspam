package org.kohsuke.stopforumspam;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kohsuke Kawaguchi
 */
public class Builder {
    private final StopForumSpam parent;
    private final List<Type> types = new ArrayList<Type>();
    private final List<String> values = new ArrayList<String>();

    Builder(StopForumSpam parent) {
        this.parent = parent;
    }

    public Builder ip(String value) {
        return add(Type.IP,value);
    }

    public Builder email(String value) {
        return add(Type.EMAIL,value);
    }

    public Builder username(String value) {
        return add(Type.USERNAME,value);
    }

    public Builder add(Type type, String value) {
        types.add(type);
        values.add(value);
        return this;
    }

    public List<Answer> query() throws IOException, SAXException {
        StringBuilder buf = new StringBuilder();
        for (int i=0; i<types.size(); i++) {
            if (buf.length()>0) buf.append('&');
            buf.append(types.get(i).asQueryString(values.get(i)));
        }
        return parent.request(buf.toString());
    }
}
