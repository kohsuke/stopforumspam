package org.kohsuke.stopforumspam;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Kohsuke Kawaguchi
 */
public class StopForumSpam {
    /**
     * Makes a single query and obtains the answer.
     */
    public Answer query(Type type, String value) throws IOException, SAXException {
        return request(type.asQueryString(value)).get(0);
    }

    public Answer ip(String value) throws IOException, SAXException {
        return query(Type.IP, value);
    }

    public Answer email(String value) throws IOException, SAXException {
        return query(Type.EMAIL, value);
    }

    public Answer username(String value) throws IOException, SAXException {
        return query(Type.USERNAME, value);
    }

    /**
     * For bulk-query.
     */
    public Builder build() {
        return new Builder(this);
    }

    List<Answer> request(String query) throws SAXException, IOException {
        try {
            URL url = new URL(String.format("http://www.stopforumspam.com/api?%s&f=xmlcdata", query));
            List<Answer> answers = new ArrayList<Answer>();

            Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
            for (Node child = dom.getDocumentElement().getFirstChild(); child!=null; child=child.getNextSibling()) {
                if (child instanceof Element) {
                    Element e = (Element) child;
                    String name = e.getTagName();
                    if (name.equals("success")) {
                        if (!getTextValue(e).equals("1"))
                            throw new IOException("Request failed");
                    }
                    if (name.equals("ip") || name.equals("username") || name.equals("email")) {
                        answers.add(parseAnswer(e));
                    }
                }
            }

            return answers;
        } catch (ParserConfigurationException e) {
            throw new Error(e);
        }
    }

    private Answer parseAnswer(Element parent) {
        Answer a = new Answer();
        a.setType(Type.valueOf(parent.getTagName().toUpperCase(Locale.ENGLISH)));
        for (Node child=parent.getFirstChild(); child!=null; child=child.getNextSibling()) {
            if (child instanceof Element) {
                Element e = (Element) child;
                String name = e.getTagName();
                String v = getTextValue(e);

                if (name.equals("value")) {
                    a.setValue(v);
                }
                if (name.equals("lastseen")) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        a.setLastSeen(format.parse(v));
                    } catch (ParseException e1) {
                        throw new IllegalArgumentException("Unexpected date format: "+ v);
                    }
                }
                if (name.equals("frequency")) {
                    a.setFrequency(Integer.parseInt(v));
                }
                if (name.equals("appears")) {
                    a.setAppears(!v.equals("0"));
                }
            }
        }
        return a;
    }

    private String getTextValue(Element e) {
        StringBuilder buf = new StringBuilder();
        for (Node child=e.getFirstChild(); child!=null; child=child.getNextSibling()) {
            if (child instanceof CharacterData) {
                CharacterData cd = (CharacterData) child;
                buf.append(cd.getData());
            }
        }
        return buf.toString();
    }
}
