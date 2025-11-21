package com.qswar.hc.enumurator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class EmpAuthorized implements Serializable {
    private final List<String> authorized;

    public EmpAuthorized(List<String> authorized) {
        this.authorized = authorized;
    }
    public boolean addNewAuthorization(Authorize authorize) {
        if (authorize == null)
            return false;
        assert authorized != null;
        authorized.add(authorize.name());
        return true;
    }

    public boolean deleteAuthorization(Authorize authorize) {
        assert authorized != null;
        if( authorized.contains(authorize.name())){
            authorized.remove(authorize.name());
            return true;
        }
        return false;
    }

    public boolean isAuthorized(Authorize authorize) {
        assert authorized != null;
        return authorized.contains(authorize.name());
    }
}
