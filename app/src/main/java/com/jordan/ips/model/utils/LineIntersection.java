package com.jordan.ips.model.utils;

public class LineIntersection {

    /**
     * https://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
     */
    // Returns 1 if the lines intersect, otherwise 0. In addition, if the lines
// intersect the intersection point may be stored in the floats i_x and i_y.
    public static boolean  get_line_intersection(double p0_x, double p0_y, double p1_x, double p1_y,
                                          double p2_x, double p2_y, double p3_x, double p3_y)
    {
        double s02_x, s02_y, s10_x, s10_y, s32_x, s32_y, s_numer, t_numer, denom, t;
        s10_x = p1_x - p0_x;
        s10_y = p1_y - p0_y;
        s32_x = p3_x - p2_x;
        s32_y = p3_y - p2_y;

        denom = s10_x * s32_y - s32_x * s10_y;
        if (denom == 0)
            return false; // Collinear
        boolean denomPositive = denom > 0;

        s02_x = p0_x - p2_x;
        s02_y = p0_y - p2_y;
        s_numer = s10_x * s02_y - s10_y * s02_x;
        if ((s_numer < 0) == denomPositive)
            return false; // No collision

        t_numer = s32_x * s02_y - s32_y * s02_x;
        if ((t_numer < 0) == denomPositive)
            return false; // No collision

        return ((s_numer > denom) != denomPositive) && ((t_numer > denom) != denomPositive); // No collision
        // Collision detected
    }
}
