/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.com.tasks.local;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javafx.concurrent.Task;
import mz.com.expert.AttributeType;
import mz.com.model.Topic;
import mz.com.model.Value;

/**
 *
 * @author Lenovo
 */
public class PublishTask extends Task<List<String>> {

    private AttributeType rootObj;

    private int index = 1;

    public PublishTask(AttributeType rootObj) {
        this.rootObj = rootObj;
    }

    @Override
    protected List<String> call() throws Exception {

        updateProgress(-1.0, 100);
        updateMessage("deleting previuos ES...");
        try {
            Path currentRelativePath = Paths.get("ES/Topics/");

            try {
                Files.newDirectoryStream(currentRelativePath.toAbsolutePath()).forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            createIndex(rootObj);

            List<Topic> topics = new ArrayList<>();

            updateMessage("Creating topics...");
            createTopics(rootObj, topics);

            index = 1;

            updateMessage("Expert system pubished");
            updateProgress(0, 100);

            return new ArrayList<>();
        } catch (Exception e) {

            updateMessage("Unsucceful!");
            updateProgress(0, 100);
            cancel(true);
            return null;
        }
    }

    public void createTopics(AttributeType rootObj, List<Topic> topics) {

        for (AttributeType node : rootObj.getAttribute()) {

            if (!node.getType().equals("V")) {

                System.out.println("topic" + index + ".htm");

                Topic topic = new Topic();
                topic.setPageName("topic" + index + ".htm");

                if (node.getType().equals("C")) {
                    topic.setConcluding(true);
                    topic.setRecommendation(node.getRecommendation());

                }

                topic.setAttribute(node.getName());
                topic.setId(node.getId());
                System.out.println(node.getName());

                List<Value> values = new ArrayList<>();

                System.out.println("---------------> size of values: " + node.getAttribute().size());
                for (AttributeType n : node.getAttribute()) {
                    Value value = new Value();

                    value.setValue(n.getName());

                    if (!n.getAttribute().isEmpty()) {
                        value.setLinkedAtrribute(n.getAttribute().get(0).getId());

                    } else {
                        System.out.println("  --" + " is empty");
                    }
                    System.out.println("  -" + n);
                    System.out.println("  --" + n.getAttribute());

                    values.add(value);

                }
                index++;

                topic.setValues(values);

                topics.add(topic);

            }

            createTopics(node, topics);

        }

        for (int k = 0; k < topics.size(); k++) {

            Topic topic = topics.get(k);

            for (int j = 0; j < topic.getValues().size(); j++) {

                Value value = topic.getValues().get(j);

                for (int z = 0; z < topics.size(); z++) {
                    if (value.getLinkedAtrribute() != null) {
                        if (value.getLinkedAtrribute().equals(topics.get(z).getId())) {

                            value.setLink(topics.get(z).getPageName());
                            System.out.println(value.getLinkedAtrribute() + " ============ " + topics.get(z).getId());
                            System.out.println(value.getValue() + " Linked to " + topics.get(z).getPageName());
                        }
                    }

                }

            }
        }

        for (Topic topic : topics) {
            createTopic(topic);
        }
    }

    public void createIndex(AttributeType rootObj) {

        try {

            String fileName = "topic" + 0 + ".htm";
            File f = new File("ES/Topics/" + fileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write("<html>");
            bw.write("<style type=\"text/css\">body {\n"
                    + "    color: darkblue;\n"
                    + "    background-color: #d8da3d;\n"
                    + "    margin: 10px;\n"
                    + "    padding: 10px; \n"
                    + "}\n"
                    + "</style>");

            bw.write("<body>");

            bw.write("<h2> <span style = \"color:blue\">An (" + rootObj.getName()
                    + "), created by: </span> <span style = \"color:red\">" + rootObj.getCreator() + "</p> </span>");
            bw.write("<p>" + rootObj.getDescription() + "</p> ");
            bw.write(" <a href=\"topic1.htm\">Start for (" + rootObj.getConclusion() + ")</a> </h2>");

            bw.write("</body>");
            bw.write("</html>");

            bw.close();
        } catch (Exception e) {
        }

    }

    public void createTopic(Topic topic) {

        try {

            String fileName = topic.getPageName();
            File f = new File("ES/Topics/" + fileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write("<html>");

            bw.write("<style type=\"text/css\">body {\n"
                    + "    color: darkblue;\n"
                    + "    background-color: #d8da3d;\n"
                    + "    margin: 10px;\n"
                    + "    padding: 10px; \n"
                    + "}\n"
                    + "</style>");
            bw.write("<body>");

            bw.write("<h2>");

            bw.write("<br>");
            if (topic.isConcluding()) {
                bw.write("<p>" + " <span style = \"color:blue\"> Based on option you have given its found this as the most approprieted conclusion: " + " </span></p>");
                bw.write("<p> Confidence index: " + this.rootObj.getConfidence() + " %</p>");

                String recLink = "";
                try {
                    recLink = "rec" + topic.getId() + ".htm";
                    File rec = new File("ES/Topics/"+recLink);
                    BufferedWriter recBw = new BufferedWriter(new FileWriter(rec));

                    recBw.write(topic.getRecommendation());
                    
                    recBw.close();
                } catch (Exception e) {
                } finally {
                    if (!recLink.isEmpty()) {
                        bw.write("<a href=\"" + recLink + "\"  style=\"margin-left:10px\">" + "See Recommedations" + "</a>");
                    }

                }

            }

            bw.write("<p> <span style = \"color:blue\">" + topic.getAttribute() + "</span></p>");

            bw.write("<br>");

            for (Value v : topic.getValues()) {

                if (v.getLink() != null) {
                    bw.write("<a href=\"" + v.getLink() + "\" style=\"margin-left:10px\">" + v.getValue() + "</a>");
                } else {
                    bw.write("<a href=\"" + topic.getPageName() + "\"  style=\"margin-left:10px\">" + v.getValue() + "</a>");
                }

                bw.write("<br>");
            }

            bw.write("</h2>");
            bw.write("</body>");
            bw.write("</html>");

            bw.close();

        } catch (Exception e) {
        }

    }

}
