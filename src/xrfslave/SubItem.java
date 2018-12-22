/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

/**
 *
 * @author jonat
 */
public class SubItem {
    public String sample_no;
    public String data_teste;
    public String nome;
    public String part_number;
    public String operator;
    public String judge;
    public String cd_judge;
    public String pb_judge;
    public String hg_judge;
    public String br_judge;
    public String cr_judge;
    public int cd_conc;
    public int pb_conc;
    public int hg_conc;
    public int br_conc;
    public int cr_conc;
    public String recipe_name;

    public SubItem(String sample_no,String data_teste, String nome, String part_number, String operator, String judge, String cd_judge, String pb_judge, String hg_judge, String br_judge, String cr_judge,int cd_conc, int pb_conc, int hg_conc, int br_conc, int cr_conc,String recipe_name ) {
        this.sample_no = sample_no;
        this.data_teste = data_teste;
        this.nome = nome;
        this.part_number = part_number;
        this.operator = operator;
        this.judge = judge;
        this.cd_judge = cd_judge;
        this.pb_judge = pb_judge;
        this.hg_judge = hg_judge;
        this.br_judge = br_judge;
        this.cr_judge = cr_judge;
        this.cd_conc = cd_conc;
        this.pb_conc = pb_conc;
        this.hg_conc = hg_conc;
        this.br_conc = br_conc;
        this.cr_conc = cr_conc;
        this.recipe_name = recipe_name;
    }
    
    
}
