package com.example.myapplication;

import com.example.myapplication.database.company.Company;

import java.util.ArrayList;

/**
 * Utility class for showing strings in adapter
 */
public class CompanyList {
    // list of companies
    private ArrayList<Company> companies;

    public CompanyList(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public CompanyList() {

    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public ArrayList<String> getCompaniesNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Company company : companies) {
            names.add(company.getCompanyName());
        }
        return names;
    }
}
