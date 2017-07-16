package SyntacticAnalyzer;

import java.util.Comparator;

/**
 * Created by MeryemMhamdi on 6/10/17.
 */
public class DependencyNodeComparer implements Comparator<DependencyNode> {
        @Override
        public int compare(DependencyNode x, DependencyNode y) {
            return compare(x.getId(),y.getId());
        }

        private static int compare(double a, double b) {
            return a < b ? -1
                    : a > b ? 1
                    : 0;
        }
}
