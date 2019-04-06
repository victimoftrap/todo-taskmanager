package it.sevenbits.backend.taskmanager.core.service.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for task order validation
 */
public class SortingOrderValidator implements Verifiable<String> {
    private List<String> orders;

    /**
     * Create validator
     */
    public SortingOrderValidator() {
        orders = new ArrayList<>();
        Collections.addAll(orders, "asc", "desc");
    }

    @Override
    public boolean verify(final String param) {
        if (param == null) {
            return false;
        }
        return orders.contains(param.toLowerCase());
    }
}
