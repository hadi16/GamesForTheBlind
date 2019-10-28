package gamesforblind.logger;

import gamesforblind.Constants;
import gamesforblind.ProgramAction;
import gamesforblind.sudoku.adapter.SudokuUnrecognizedKeyActionAdapter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

public class LogCreator {
    private static final File LOG_FILES_DIRECTORY = new File(
            Paths.get(System.getProperty("user.dir"), "logs/").toString()
    );

    private final ArrayList<ProgramAction> programActionList = new ArrayList<>();

    public LogCreator() {
        if (!LOG_FILES_DIRECTORY.exists()) {
            LOG_FILES_DIRECTORY.mkdirs();
        }
    }

    public void addProgramAction(ProgramAction actionToAdd) {
        if (!Constants.SAVE_LOGS) {
            return;
        }

        this.programActionList.add(actionToAdd);
    }

    public void saveProgramActions() {
        if (!Constants.SAVE_LOGS) {
            return;
        }

        try {
            for (ProgramAction action : this.programActionList) {
                Marshaller jaxbMarshaller = JAXBContext.newInstance(action.getClass()).createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

                XmlAdapter actionAdapter = action.getJaxbAdapter();
                if (actionAdapter != null) {
                    jaxbMarshaller.setAdapter(actionAdapter);
                }

                StringWriter stringWriter = new StringWriter();
                jaxbMarshaller.marshal(action, stringWriter);
                System.out.println(stringWriter.toString());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        System.out.println("Saved " + this.programActionList.size() + " actions to the logs directory!");
    }
}
