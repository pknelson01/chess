package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;

public class ClearService extends Service {

    public ClearService(DataAccess dataAccess) {
        super(dataAccess);
    }

    public void clear() throws DataAccessException {
        dataAccess.clear();
    }
}
