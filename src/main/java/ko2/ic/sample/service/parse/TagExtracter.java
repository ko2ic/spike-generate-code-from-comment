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
package ko2.ic.sample.service.parse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ko2.ic.sample.exceptions.SystemException;
import ko2.ic.sample.model.Tag;
import ko2.ic.sample.valueobject.Line;

import org.xml.sax.SAXException;

import com.google.common.base.Throwables;

/**
 * Extracts Tag.<br>
 * @author kouji ishii
 */
public class TagExtracter {

    private final Tag rootTag = new Tag();

    private final List<Tag> itemsTags = new ArrayList<>();

    private final List<Tag> deleteTags = new ArrayList<>();

    public Tag getRootTag() {
        return rootTag;
    }

    public List<Tag> getItemsTags() {
        return itemsTags;
    }

    public List<Tag> getDeleteTags() {
        return deleteTags;
    }

    public TagExtracter(List<Line> tagLines) {
        StringBuilder sb = new StringBuilder();
        TagClosureCreator.TagClosure itemsHolder = new TagClosureCreator().getHolder();
        TagClosureCreator.TagClosure deleteHolder = new TagClosureCreator().getHolder();

        for (Line line : tagLines) {
            sb.append(line.toString());
            if (line.isRootTagStartLine()) {
                rootTag.setStartLineNumber(line.getLineNumber());
            } else if (line.isRootTagEndLine()) {
                rootTag.setEndLineNumber(line.getLineNumber());
            } else if (line.isItemsTagStartLine()) {
                itemsHolder.getValue().setStartLineNumber(line.getLineNumber());
            } else if (line.isItemsTagEndLine()) {
                itemsHolder.getValue().setEndLineNumber(line.getLineNumber());
                itemsTags.add(itemsHolder.getValue().clone());
                itemsHolder.clear();
            } else if (line.isDeleteTagStartLine()) {
                deleteHolder.getValue().setStartLineNumber(line.getLineNumber());
            } else if (line.isDeleteTagEndLine()) {
                deleteHolder.getValue().setEndLineNumber(line.getLineNumber());
                deleteTags.add(deleteHolder.getValue().clone());
                deleteHolder.clear();
            }
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            InputStream is = new ByteArrayInputStream(sb.toString().getBytes());
            TagHandler tagHandler = new TagHandler();
            parser.parse(is, tagHandler);
            Tag generatorTag = tagHandler.getRootTag();
            this.rootTag.setBinds(generatorTag.getBinds());
            this.rootTag.setReplaces(generatorTag.getReplaces());

            List<Tag> itemsTags = tagHandler.getItemsTag();
            for (int i = 0; i < itemsTags.size(); i++) {
                this.itemsTags.get(i).setBinds(itemsTags.get(i).getBinds());
                this.itemsTags.get(i).setReplaces((itemsTags.get(i).getReplaces()));
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw Throwables.propagate(new SystemException(e));
        }
    }

    private static class TagClosureCreator {
        public TagClosure getHolder() {
            final Tag value = new Tag();

            return new TagClosure() {
                @Override
                public Tag getValue() {
                    return value;
                }

                @Override
                public void clear() {
                    value.setStartLineNumber(0);
                    value.setEndLineNumber(0);
                }
            };
        }

        public interface TagClosure {
            Tag getValue();

            void clear();
        }
    }
}
