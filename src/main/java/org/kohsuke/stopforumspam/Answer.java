package org.kohsuke.stopforumspam;

import java.util.Date;

/**
 * Answer to the query.
 *
 * @author Kohsuke Kawaguchi
 */
public class Answer {
    private Type type;
    private String value;
    private Date lastSeen;
    private int frequency;
    private boolean appears;

    /**
     * Type of the query.
     */
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Does this record appear in the spam database?
     */
    public boolean isAppears() {
        return appears;
    }

    public void setAppears(boolean appears) {
        this.appears = appears;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * When did this user last reported in the spam database?
     *
     * null if {@link #isAppears()} is false.
     */
    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    /**
     * The actual value of the query for {@linkplain #getType() the specified query type}
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "appears=" + appears +
                ", type=" + type +
                ", value=" + value +
                ", lastSeen=" + lastSeen +
                ", frequency=" + frequency +
                '}';
    }
}
