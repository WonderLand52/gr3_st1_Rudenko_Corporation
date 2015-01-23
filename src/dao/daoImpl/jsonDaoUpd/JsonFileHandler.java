package dao.daoImpl.jsonDaoUpd;


import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.UUID;

public class JsonFileHandler {

    protected static void writeJson(JSONArray arr, String fileName, boolean append) throws DaoSystemException, NoSuchEntityException {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, append))) {
            bw.write(arr.toString());
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String readJson(String fileName) throws DaoSystemException, NoSuchEntityException {

        StringBuffer sb =  new StringBuffer();
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }catch (FileNotFoundException e) {
            throw new NoSuchEntityException("There is no file with such a name!" + e.getMessage());
        } catch (IOException e) {
            throw new DaoSystemException("Error while reading file: " + e.getMessage());
        }

        return sb.toString();
    }

    public JSONArray getArrayObjectsFromJson(String fileName) throws DaoSystemException, NoSuchEntityException {
        JSONArray objectsJson;
        if(fileExists(fileName)){
            String objectsStr = readJson(fileName);
            JSONParser parser = new JSONParser();

            try {
                objectsJson = (JSONArray) parser.parse(objectsStr);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new DaoSystemException("Error while parsing file: " + e.getMessage());
            }

        } else {
            objectsJson = new JSONArray();
        }

        System.out.println("Inside getArrayObjectsFromJson objectsJson: " + objectsJson);
        return objectsJson;
    }

    public UUID getUUIDFromString(String str) {
        return UUID.fromString(str);
    }

    public boolean fileExists(String fileName) {
        System.out.println(fileName);
        return new File(fileName).exists();
    }

    public static void closeJson() {

        try {
            System.out.println("Inside closeJson");
            if (ProjJsonDAO.projChanged) {
                System.out.println("ProjectJsonDAO: " + ProjJsonDAO.projChanged);
                writeJson(ProjJsonDAO.getInstance().modulesJson, ProjJsonDAO.FILE_NAME_PROJECT, false);
                ProjJsonDAO.projChanged = false;
            }

            if (EmpJsonDAO.empChanged) {
                System.out.println("EMployeeJsonDAO: " + EmpJsonDAO.empChanged);
                writeJson(EmpJsonDAO.getInstance().modulesJson, EmpJsonDAO.FILE_NAME_EMPLOYEE, false);
                EmpJsonDAO.empChanged = false;
            }

            if (DeptJsonDAO.deptChanged) {
                System.out.println("DepartmentJsonDAO: " + DeptJsonDAO.deptChanged);
                writeJson(DeptJsonDAO.getInstance().modulesJson, DeptJsonDAO.FILE_NAME_JSON_DEPARTMENT, false);
                DeptJsonDAO.deptChanged = false;
            }

            if (EmpProjJsonDAO.alive) {
                System.out.println("EmpProjJsonDAO: " + EmpProjJsonDAO.alive);
                writeJson(EmpProjJsonDAO.getInstance().idsJson, EmpProjJsonDAO.FILE_NAME_JSON_PROJECT_IDS, false);
                EmpProjJsonDAO.alive = false;
            }

            if (DeptEmpJsonDAO.alive) {
                System.out.println("DeptEmpJsonDAO: " + DeptEmpJsonDAO.alive);
                writeJson(DeptEmpJsonDAO.getInstance().idsJson, DeptEmpJsonDAO.FILE_NAME_DEPARTMENT_IDS, false);
                DeptEmpJsonDAO.alive = false;
            }

            if (EventJsonDAO.changed) {
                writeJson(EventJsonDAO.getInstance().eventsJson, EventJsonDAO.FILE_NAME_JSON_EVENT, false);
            }

        }catch (DaoSystemException ex) {
            ex.printStackTrace();
        }catch (NoSuchEntityException ex) {
            ex.printStackTrace();
        }
    }
}
