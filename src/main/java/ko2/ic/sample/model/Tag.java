/*******************************************************************************
 * Copyright (c) 2014
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Kouji Ishii - initial implementation
 *******************************************************************************/
package ko2.ic.sample.model;

import ko2.ic.sample.exceptions.SystemException;

import com.google.common.base.Throwables;

/**
 * Presents Tag.<br>
 * @author kouji ishii
 */
public class Tag implements Cloneable {

    private int startLineNumber;

    private int endLineNumber;

    private String[] binds;

    private String[] replaces;

    /**
     * startLineNumberを取得する<br>
     * @return startLineNumber
     */
    public int getStartLineNumber() {
        return startLineNumber;
    }

    /**
     * startLineNumberを設定する<br>
     * @param startLineNumber startLineNumber
     */
    public void setStartLineNumber(int startLineNumber) {
        this.startLineNumber = startLineNumber;
    }

    /**
     * endLineNumberを取得する<br>
     * @return endLineNumber
     */
    public int getEndLineNumber() {
        return endLineNumber;
    }

    /**
     * endLineNumberを設定する<br>
     * @param endLineNumber endLineNumber
     */
    public void setEndLineNumber(int endLineNumber) {
        this.endLineNumber = endLineNumber;
    }

    /**
     * bindTargetsを取得する<br>
     * @return bindTargets
     */
    public String[] getBinds() {
        return binds;
    }

    /**
     * bindTargetsを設定する<br>
     * @param binds bindTargets
     */
    public void setBinds(String bindTarget) {
        this.binds = bindTarget.split(",");
    }

    public void setBinds(String[] bindTargets) {
        this.binds = bindTargets;
    }

    /**
     * replacementを取得する<br>
     * @return replacement
     */
    public String[] getReplaces() {
        return replaces;
    }

    /**
     * replacementを設定する<br>
     * @param replacement replacement
     */
    public void setReplaces(String replacement) {
        this.replaces = replacement.split(",");
    }

    public void setReplaces(String[] replacements) {
        this.replaces = replacements;
    }

    @Override
    public Tag clone() {
        try {
            Tag tag = (Tag) super.clone();
            tag.setBinds(getBinds());
            tag.setReplaces(getReplaces());
            tag.setStartLineNumber(getStartLineNumber());
            tag.setEndLineNumber(getEndLineNumber());
            return tag;
        } catch (CloneNotSupportedException e) {
            throw Throwables.propagate(new SystemException(e));
        }
    }

    public enum TagType {
        ROOT, ITEMS, DELETE;

        public boolean equals(String tagName) {
            return this.name().equalsIgnoreCase(tagName);
        }
    }
}
