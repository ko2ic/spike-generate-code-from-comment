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

import java.util.ArrayList;
import java.util.List;

import ko2.ic.sample.model.Tag;
import ko2.ic.sample.model.Tag.TagType;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handles Tag with SaxParser<br>
 * @author kouji ishii
 */
public class TagHandler extends DefaultHandler {

    private static final String BIND_ATTR_NAME = "bind";

    private static final String REPLACE_ATTR_NAME = "replace";

    private final Tag rootTag = new Tag();

    private final List<Tag> itemsTag = new ArrayList<>();

    private final List<Tag> deleteTags = new ArrayList<>();

    public Tag getRootTag() {
        return rootTag;
    }

    /**
     * itemsTagを取得する<br>
     * @return itemsTag
     */
    public List<Tag> getItemsTag() {
        return itemsTag;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (TagType.ROOT.equals(qName)) {
            for (int i = 0; i < attributes.getLength(); i++) {
                if (BIND_ATTR_NAME.equalsIgnoreCase(attributes.getQName(i))) {
                    rootTag.setBinds(attributes.getValue(i));
                } else if (REPLACE_ATTR_NAME.equalsIgnoreCase(attributes.getQName(i))) {
                    rootTag.setReplaces(attributes.getValue(i));
                }
            }
        } else {
            if (TagType.ITEMS.equals(qName)) {
                Tag tag = new Tag();
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (BIND_ATTR_NAME.equalsIgnoreCase(attributes.getQName(i))) {
                        tag.setBinds(attributes.getValue(i));
                    } else if (REPLACE_ATTR_NAME.equalsIgnoreCase(attributes.getQName(i))) {
                        tag.setReplaces(attributes.getValue(i));
                    }
                }
                itemsTag.add(tag);
            } else if (TagType.DELETE.equals(qName)) {
                Tag tag = new Tag();
                deleteTags.add(tag);
            }
        }
    }
}
