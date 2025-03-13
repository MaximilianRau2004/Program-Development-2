package de.unistuttgart.iste.ese.api.Models;

import jakarta.xml.bind.JAXBException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.OutputField;
import org.jpmml.evaluator.TargetField;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for loading and using a PMML-based Todo classification model.
 */
public class TodoModel {
    private static final Log LOG = LogFactory.getLog(TodoModel.class);
    private Evaluator evaluator;

    /**
     * Constructs a TodoModel with the specified PMML model file path.
     *
     * @param pathname The path to the PMML model file.
     */
    public TodoModel(String pathname) {
        loadModel();
    }

    /**
     * Loads the PMML model from the specified file path and initializes the model
     * evaluator.
     * If the loading process encounters any exceptions, the evaluator is set to
     * null.
     */
    public void loadModel() {
        // Building a model evaluator from a PMML file
        try {
            this.evaluator = new LoadingModelEvaluatorBuilder()
                .load(getClass().getClassLoader().getResourceAsStream("model.pmml"))
                .build();
        } catch (ParserConfigurationException | SAXException | JAXBException e) {
            LOG.error("Could not load AI model:", e);
            this.evaluator = null;
        }
    }

    /**
     * Parses the model's output to retrieve the predicted class/category.
     *
     * @param results The results obtained from evaluating the model on input data.
     * @return The predicted class/category based on the model's output.
     */
    private String parseModelOutput(Map<String, ?> results) {
        // Primary result (y) field(s)
        List<TargetField> targetFields = this.evaluator.getTargetFields();
        // Secondary result (eg. probability(y), decision(y)) fields
        List<OutputField> outputFields = this.evaluator.getOutputFields();

        int predictedLabelIndex = Integer.parseInt(results.get(outputFields.get(2).getName()).toString());
        String[] targetLabels = targetFields.get(0).getName().replaceAll("[\\['\\]]", "").split(" ");
        return targetLabels[predictedLabelIndex];
    }

    /**
     * Predicts the class/category of a given input text.
     *
     * @param inputString The input text to be classified.
     * @return The predicted class/category for the input text.
     */
    public String predictClass(String inputString) {
        if (evaluator == null) {
            LOG.warn("Cannot predict class without a loaded model");
            return "unknown";
        }

        // Prepare the input data
        Map<String, Object> input = new HashMap<>();
        input.put("text", inputString);

        // Predict class and parse results
        Map<String, ?> results = this.evaluator.evaluate(input);

        return parseModelOutput(results);
    }

    /**
     * Unloads the loaded PMML model, releasing resources.
     */
    public void unloadModel() {
        this.evaluator = null;
    }
}
