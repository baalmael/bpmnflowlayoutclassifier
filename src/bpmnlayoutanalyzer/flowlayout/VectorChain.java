package bpmnlayoutanalyzer.flowlayout;


import java.util.ArrayList;
import java.util.List;

import static bpmnlayoutanalyzer.flowlayout.Constants.*;
import static bpmnlayoutanalyzer.flowlayout.Vector.add;
import static bpmnlayoutanalyzer.flowlayout.Vector.smallestPositiveAngle;

/**
 * A VectorChain is a list of Vectors, that can be simplified (combining subsequent Vectors with similar directions) and discretized.
 */
public class VectorChain {
    private static final double ANGLE_THRESHOLD = (Math.PI / 180) * 22.5; //22,5 Degrees
    private List<Vector> vectors;

    public VectorChain() {
        vectors = new ArrayList<>();
    }

    public void addVector(Vector v) {
        vectors.add(v);
    }

    @Override
    public String toString() {
        return vectors.toString();
    }

    public List<Vector> getVectors() {
        return vectors;
    }

    public String getDiscreteVectorDirections() {
        double maxlength = vectors.stream().map(Vector::getLength).max(Double::compare).get();
        return vectors.stream()
                .map(v -> discreteDirection(v, v.getLength() >= maxlength / 2)).reduce(String::concat).get();
    }

    private String discreteDirection(Vector v, boolean isLong) {
        double pis = Math.PI / 16;
        double d = v.getDirection();
        boolean m = v.isMarked();
        boolean l = isLong;
        if (d < -15 * pis) return m ? l ? W_M_L : W_M_S : l ? W_NM_L : W_NM_S;
        if (d < -13 * pis) return m ? l ? NWW_M_L : NWW_M_S : l ? NWW_NM_L : NWW_NM_S;
        if (d < -11 * pis) return m ? l ? NW_M_L : NW_M_S : l ? NW_NM_L : NW_NM_S;
        if (d < -19 * pis) return m ? l ? NNW_M_L : NNW_M_S : l ? NNW_NM_L : NNW_NM_S;
        if (d < -7 * pis) return m ? l ? N_M_L : N_M_S : l ? N_NM_L : N_NM_S;
        if (d < -5 * pis) return m ? l ? NNE_M_L : NNE_M_S : l ? NNE_NM_L : NNE_NM_S;
        if (d < -3 * pis) return m ? l ? NE_M_L : NE_M_S : l ? NE_NM_L : NE_NM_S;
        if (d < -1 * pis) return m ? l ? NEE_M_L : NEE_M_S : l ? NEE_NM_L : NEE_NM_S;
        if (d < 1 * pis) return m ? l ? E_M_L : E_M_S : l ? E_NM_L : E_NM_S;
        if (d < 3 * pis) return m ? l ? SEE_M_L : SEE_M_S : l ? SEE_NM_L : SEE_NM_S;
        if (d < 5 * pis) return m ? l ? SE_M_L : SE_M_S : l ? SE_NM_L : SE_NM_S;
        if (d < 7 * pis) return m ? l ? SSE_M_L : SSE_M_S : l ? SSE_NM_L : SSE_NM_S;
        if (d < 9 * pis) return m ? l ? S_M_L : S_M_S : l ? S_NM_L : S_NM_S;
        if (d < 11 * pis) return m ? l ? SSW_M_L : SSW_M_S : l ? SSW_NM_L : SSW_NM_S;
        if (d < 13 * pis) return m ? l ? SW_M_L : SW_M_S : l ? SW_NM_L : SW_NM_S;
        if (d < 15 * pis) return m ? l ? SWW_M_L : SWW_M_S : l ? SWW_NM_L : SWW_NM_S;
        else return m ? l ? W_M_L : W_M_S : l ? W_NM_L : W_NM_S;
    }


    public void simplify() {
        if (vectors.size() == 1) return;
        List<Vector> simplified = new ArrayList<>();
        boolean simplifiable = false;
        for (int i = 0; i < vectors.size() - 1; i++) {
            Vector v1 = vectors.get(i);
            Vector v2 = vectors.get(i + 1);
            double angle = smallestPositiveAngle(v1, v2);
            if (angle < ANGLE_THRESHOLD) {
                simplified.add(add(v1, v2));
                simplifiable = true;
                i++;
            } else {
                simplified.add(v1);
            }
            if (i + 1 == vectors.size() - 1) {
                simplified.add(vectors.get(vectors.size() - 1));
            }
        }
        vectors = simplified;
        if (simplifiable) {
            simplify();
        }
    }

    public Vector lastVector() {
        if (vectors.size() == 0) return null;
        return vectors.get(vectors.size() - 1);
    }

    public double getCombinationError() {
        return vectors.stream().map(Vector::getAverageCombinationError).mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public double letterAccuracyError() {
        return vectors.stream().map(Vector::getDirection).map(direction -> {
            // Expecting 16 directions
            double mod = Math.abs(direction % (Math.PI / 8));
            return Math.min(mod, Math.PI / 8 - mod);
        }).mapToDouble(Double::doubleValue).average().orElse(0);
    }
}
