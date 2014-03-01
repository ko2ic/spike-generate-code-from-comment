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
package ko2.ic.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ko2.ic.sample.model.Tag;
import ko2.ic.sample.service.parse.TagExtracter;
import ko2.ic.sample.valueobject.Code;

public class App {

    public static void main(String[] args) {
        StringBuilder filePath = new StringBuilder();
        filePath.append("src").append(File.separator);
        filePath.append("main").append(File.separator);
        filePath.append("java").append(File.separator);
        filePath.append("sample").append(File.separator);
        filePath.append("ParserTarget.java");

        try (InputStream is = new FileInputStream(filePath.toString())) {
            Code code = new Code(is);

            TagExtracter extractor = new TagExtracter(code.getTagLines());

            Tag rootTag = extractor.getRootTag();
            List<Tag> itemsTags = extractor.getItemsTags();
            List<Tag> deleteTags = extractor.getDeleteTags();

            String first = code.getCodeBeforeRootTag(rootTag.getStartLineNumber(), deleteTags);
            String last = code.getCodeAfterRootTag(rootTag.getEndLineNumber(), deleteTags);
            String middle = code.getCodeInsideRootTag(rootTag, itemsTags, deleteTags);

            System.out.println("--------------------");
            System.out.print(first.toString());
            System.out.print(middle.toString());
            System.out.print(last.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        // try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath.toString())));) {
        // br.mark(10000);
        // StringBuffer sb = new StringBuffer();
        // List<String> list = new ArrayList<>();
        //
        // for (String line = br.readLine(); line != null; line = br.readLine()) {
        // list.add(line + "\n");
        // sb.append(line + "\n");
        // }
        //
        // ASTParser parser = ASTParser.newParser(AST.JLS4);
        // parser.setSource(sb.toString().toCharArray());
        // CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());
        // // unit.recordModifications();
        //
        // CommentVisitor visitor = new CommentVisitor(unit, list.toArray(new String[list.size()]));
        // unit.accept(visitor);
        //
        // Map<Integer, String> commentMap = visitor.getCommentMap();
        // TagExtracter holder = new TagExtracter(commentMap);
        //
        // Tag generatorTag = holder.getGeneratorTag();
        // List<Tag> itemsTags = holder.getItemsTags();
        // List<Tag> deleteTags = holder.getDeleteTags();
        //
        // br.reset();
        //
        // StringBuilder first = new StringBuilder();
        // StringBuilder last = new StringBuilder();
        // StringBuilder middle = new StringBuilder();
        // String line = br.readLine();
        // for (int i = 1; line != null; line = br.readLine(), i++) {
        // boolean skip = false;
        // for (Tag deleteTagInfo : deleteTags) {
        // if (deleteTagInfo.getStartLineNumber() <= i && deleteTagInfo.getEndLineNumber() >= i) {
        // skip = true;
        // }
        // }
        //
        // if (skip) {
        // continue;
        // }
        //
        // if (generatorTag.getStartLineNumber() > i) {
        // first.append(line).append("\n");
        // } else if (generatorTag.getEndLineNumber() < i) {
        // last.append(line).append("\n");
        // } else if (generatorTag.getStartLineNumber() < i && generatorTag.getEndLineNumber() > i) {
        // if (generatorTag.getBinds().length != generatorTag.getReplaces().length) {
        // throw new IllegalStateException();
        // }
        // for (int j = 0; j < generatorTag.getBinds().length; j++) {
        // line = line.replace(generatorTag.getBinds()[j], generatorTag.getReplaces()[j]);
        // }
        // for (Tag tag : itemsTags) {
        // if (tag.getStartLineNumber() < i && tag.getEndLineNumber() > i) {
        // if (tag.getBinds().length != tag.getReplaces().length) {
        // throw new IllegalStateException();
        // }
        // for (int j = 0; j < tag.getBinds().length; j++) {
        // line = line.replace(tag.getBinds()[j], tag.getReplaces()[j]);
        // }
        // }
        // }
        //
        // boolean isItemsLine = false;
        // for (Tag tag : itemsTags) {
        // if (tag.getStartLineNumber() == i || tag.getEndLineNumber() == i) {
        // isItemsLine = true;
        // }
        // }
        //
        // if (!isItemsLine) {
        // middle.append(line).append("\n");
        // }
        // }
        // }
        //
        // System.out.println("--------------------");
        // System.out.println(first.toString());
        // System.out.println(middle.toString());
        // System.out.println(last.toString());
        //
        // // System.out.println("--------------------");
        // // System.out.println(code);
        //
        // } catch (IOException e) {
        // e.printStackTrace();
        // return;
        // }
    }
    // private static String getCode(String code, CompilationUnit unit) {
    // IDocument eDoc = new Document(code);
    // TextEdit edit = unit.rewrite(eDoc, null);
    // try {
    // edit.apply(eDoc);
    // return eDoc.get();
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // return null;
    // }
    // }

}
