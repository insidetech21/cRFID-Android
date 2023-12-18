package com.example.crfid.model.materialTagPairModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class MaterialTagPair_Response {

    @SerializedName("d")
    @Expose
    private MaterialTagPair_Response.D d;

    public MaterialTagPair_Response.D getD() {
        return d;
    }

    public void setD(MaterialTagPair_Response.D d) {
        this.d = d;
    }


    public class D {

        @SerializedName("results")
        @Expose
        private List<MaterialTagPair_Item> results;

        public List<MaterialTagPair_Item> getResults() {
            return results;
        }

        public void setResults(List<MaterialTagPair_Item> results) {
            this.results = results;
        }

    }

}
