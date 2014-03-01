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
package ko2.ic.sample.valueobject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import ko2.ic.sample.exceptions.SystemException;
import ko2.ic.sample.model.Lines;
import ko2.ic.sample.model.Tag;

import com.google.common.base.Throwables;

/**
 * Holds Code.<br>
 * @author kouji ishii
 */
public class Code {

    /** all code lines */
    private final Lines lines = new Lines();

    /**
     * Reads and Holds contents of code.<br>
     * @param is java code stream
     */
    public Code(InputStream is) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is));) {
            for (String str = br.readLine(); str != null; str = br.readLine()) {
                lines.add(str);
            }
        } catch (IOException e) {
            throw Throwables.propagate(new SystemException(e));
        }
    }

    public List<Line> getTagLines() {
        return lines.getTagLines();
    }

    @Override
    public String toString() {
        return lines.toString();
    }

    public int getLineCount() {
        return lines.size();
    }

    public String getCodeInsideRootTag(Tag generatorTag, List<Tag> itemsTags, List<Tag> deleteTags) {

        StringBuilder sb = new StringBuilder();
        for (int lineNumber = 1; lineNumber <= lines.size(); lineNumber++) {
            Line line = lines.get(lineNumber);
            if (lines.inTag(deleteTags, lineNumber) || line.isDeleteTagLine()) {
                continue;
            }

            if (lines.inTag(generatorTag, lineNumber)) {
                if (generatorTag.getBinds().length != generatorTag.getReplaces().length) {
                    throw new IllegalStateException();
                }

                for (int j = 0; j < generatorTag.getBinds().length; j++) {
                    line = line.replace(generatorTag.getBinds()[j], generatorTag.getReplaces()[j]);
                }

                for (Tag tag : itemsTags) {
                    if (lines.inTag(tag, lineNumber)) {
                        if (tag.getBinds().length != tag.getReplaces().length) {
                            throw new IllegalStateException();
                        }

                        for (int j = 0; j < tag.getBinds().length; j++) {
                            line = line.replace(tag.getBinds()[j], tag.getReplaces()[j]);
                        }
                    }
                }

                if (line.isItemsTagStartLine() || line.isItemsTagEndLine()) {
                    continue;
                }

                sb.append(line);

            }
        }
        return sb.toString();
    }

    public String getCodeBeforeRootTag(final int startLine, List<Tag> deleteTags) {
        return getCodeArroundRootTag(deleteTags, new Predicate() {

            @Override
            public boolean evaluate(int lineNumber) {
                return startLine > lineNumber;
            }
        });
    }

    public String getCodeAfterRootTag(final int endLine, List<Tag> deleteTags) {
        return getCodeArroundRootTag(deleteTags, new Predicate() {

            @Override
            public boolean evaluate(int lineNumber) {
                return endLine < lineNumber;
            }
        });
    }

    private String getCodeArroundRootTag(List<Tag> deleteTags, Predicate predicate) {
        StringBuilder sb = new StringBuilder();
        for (int lineNumber = 1; lineNumber <= lines.size(); lineNumber++) {
            if (isSkip(deleteTags, lineNumber)) {
                continue;
            }

            if (predicate.evaluate(lineNumber)) {
                sb.append(lines.get(lineNumber));
            }
        }
        return sb.toString();
    }

    /**
     * whether inside delete tag.<br>
     * @param deleteTags list of ¥<delete¥>
     * @param lineNumber lineNumber
     * @return if true,skip
     */
    private boolean isSkip(List<Tag> deleteTags, int lineNumber) {
        boolean skip = false;
        for (Tag deleteTagInfo : deleteTags) {
            if (deleteTagInfo.getStartLineNumber() <= lineNumber && deleteTagInfo.getEndLineNumber() >= lineNumber) {
                skip = true;
            }
        }
        return skip;
    }

    private interface Predicate {
        boolean evaluate(int lineNumber);
    }
}
