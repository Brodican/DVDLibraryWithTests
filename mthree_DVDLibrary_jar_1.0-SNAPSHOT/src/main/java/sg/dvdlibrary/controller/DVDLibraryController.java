/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.dvdlibrary.controller;

import java.util.List;
import sg.dvdlibrary.dao.DVDLibraryDao;
import sg.dvdlibrary.dao.DVDLibraryPersistenceException;
import sg.dvdlibrary.dto.DVD;
import sg.dvdlibrary.service.DVDLibraryServiceLayer;
import sg.dvdlibrary.ui.DVDLibraryView;

/**
 *
 * @author utkua
 */
public class DVDLibraryController {

    
    private DVDLibraryView view;
    private DVDLibraryDao dao;
    
    public DVDLibraryController(DVDLibraryView view, DVDLibraryDao dao) {
        this.view = view;
        this.dao = dao;
    }
    
    // Run is public
    public void run() {
        // Loop until otherwise specified by user
        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            // Loop will run until user inputs 7
            // setting keepGoing to false.
            while (keepGoing) {
                
                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        createDVD();
                        break;
                    case 2:
                        viewDVD();
                        break;
                    case 3:
                        removeDVD();
                        break;
                    case 4:
                        editDvdField();
                        break;
                    case 5:
                        editDVD();
                        break;
                    case 6:
                        listDVDs();
                        break;
                    case 7:
                        countDVDs();
                        break;
                    case 8:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (DVDLibraryPersistenceException e) {
            // Ensure user is informed when error occurs
            view.displayErrorMessage(e.getMessage());
        }
    }

    // Print menu
    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void createDVD() throws DVDLibraryPersistenceException {
        // Make view display create banner to inform user
        view.displayCreateDVDBanner();
        String newDvdTitle = view.getTitleChoice();
        // Do not create if DVD with title already exists
        if (dao.getDVD(newDvdTitle) != null) {
            view.displayCreateFailureBanner();
        } else {
            // Get object created with user input from view
            DVD newDVD = view.getNewDVDInfo(newDvdTitle);
            // Make dao add to list of DVDs
            dao.addDVD(newDVD.getTitle(), newDVD);
            // Make view display success banner to inform user
            view.displayCreateSuccessBanner();
        }
    }
    
    // Edit DVD - when we already know it exists
    private void createDVDEdit(String title) throws DVDLibraryPersistenceException {
        DVD newDVD = view.getEditDVDInfo(title);
        dao.addDVD(newDVD.getTitle(), newDVD);
        view.displayEditSuccessBanner();
    }

    private void listDVDs() throws DVDLibraryPersistenceException {
        view.displayDisplayAllBanner();
        List<DVD> dvdList = dao.getAllDVDs();
        view.displayDVDList(dvdList);
    }

    // View specific DVD with title input by user
    private void viewDVD() throws DVDLibraryPersistenceException {
        view.displayDisplayDVDBanner();
        String title = view.getTitleChoice();
        DVD dvd = dao.getDVD(title);
        view.displayDVD(dvd);
    }

    private void removeDVD() throws DVDLibraryPersistenceException {
        view.displayRemoveDVDBanner();
        String title = view.getTitleChoice();
        DVD removedDVD = dao.removeDVD(title);
        view.displayRemoveResult(removedDVD);
    }
    
    // Ask for title and edit if exists
    // For editing entire DVD
    private void editDVD() throws DVDLibraryPersistenceException {
        view.displayEditDVDBanner();
        String title = view.getTitleChoice();
        DVD editedDVD = dao.getDVD(title);
        view.displayDVD(editedDVD);
        if (editedDVD != null) {
            createDVDEdit(title);
        }
    }
    
    // Edit a specific field
    private void editDvdField() throws DVDLibraryPersistenceException {
        // Get title - if it doesn't exist, indicate failure to user
        String title = view.getTitleChoice();
        if (dao.getDVD(title) == null) {
            view.displayEditFailureBanner();
            return;
        }
        
        // Get field to edit from user
        int field = view.printEditChoicesAndGetSelection();
        String fieldString;
        // String for new value of edited field
        String newVal;
        // Double for new value of edited field - in this case just IMDB
        double newValDouble;
        
        // Get new value, indicating field to be changed
        // Call field setting method with new value
        switch (field) {
            case 1:
                fieldString = "Release Date";
                newVal = view.getEditValue(fieldString);
                dao.setFieldDate(newVal, title);
                break;
            case 2:
                fieldString = "MPAA Rating";
                newVal = view.getEditValue(fieldString);
                dao.setFieldRating(newVal, title);
                break;
            case 3:
                fieldString = "Director's Name";
                newVal = view.getEditValue(fieldString);
                dao.setFieldDirectorName(newVal, title);
                break;
            case 4:
                fieldString = "Studio";
                newVal = view.getEditValue(fieldString);
                dao.setFieldStudio(newVal, title);
                break;
            case 5:
                fieldString = "User Rating/Note";
                newVal = view.getEditValue(fieldString);
                dao.setFieldRating(newVal, title);
                break;
            case 6:
                fieldString = "IMDB Rating";
                newValDouble = view.getEditValueImdb(fieldString);
                dao.setFieldImdb(newValDouble, title);
                break;
        }
        
        view.displayEditSuccessBanner();
    }
    
    // Display number of DVDs
    private void countDVDs() throws DVDLibraryPersistenceException {
        view.displayCountDVDBanner();
        view.displayCount(dao.countDVDs());
    }

    // Inform user of unknown command
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
    
}
