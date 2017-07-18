package SyntacticAnalyzer;

import java.util.Comparator;

/** Used for write a comparator method to compare two instances of type DependencyNode
 * @author MeryemMhamdi
 * @date 6/10/17.
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
