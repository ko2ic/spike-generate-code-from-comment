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

import java.util.ArrayList;
import java.util.List;

import ko2.ic.sample.valueobject.Line;

/**
 * Represents all lines.<br>
 * @author kouji ishii
 */
public class Lines {

    private final List<Line> list = new ArrayList<>();

    private final List<Line> tagLines = new ArrayList<>();

    private final List<Integer> lineNumberInRootTag = new ArrayList<>();

    private final List<Integer> lineNumberInItemsTag = new ArrayList<>();

    private final List<Integer> lineNumberInDeleteTag = new ArrayList<>();

    public List<Line> getTagLines() {
        return tagLines;
    }

    public void add(String str) {
        Line line = new Line(list.size() + 1, str + System.getProperty("line.separator"));
        if (line.isCommentTagLine()) {
            tagLines.add(line.replace("//", ""));
        }
        if (line.isRootTagLine()) {
            lineNumberInRootTag.add(line.getLineNumber());
        } else if (line.isItemsTagLine()) {
            lineNumberInItemsTag.add(line.getLineNumber());
        } else if (line.isDeleteTagLine()) {
            lineNumberInDeleteTag.add(line.getLineNumber());
        }
        list.add(line);
    }

    public boolean inTag(Tag tag, Integer lineNumber) {
        return tag.getStartLineNumber() < lineNumber && tag.getEndLineNumber() > lineNumber;
    }

    public boolean inTag(List<Tag> tags, Integer lineNumber) {
        for (Tag tag : tags) {
            if (inTag(tag, lineNumber)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return list.size();
    }

    public Line get(Integer lineNumber) {
        return list.get(lineNumber - 1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Line line : list) {
            sb.append(line.toString());
        }
        return sb.toString();
    }

}
