package dynamo;

/**
*	@class Factorial
*	@brief Provides an indexed array of factorials, that are precomputed at runtime
*/
public class Factorial {
    public static int factorials[];

    public static void precompute(int maxFactorials) 
    {
        int n, l1, f1;
        factorials = new int[maxFactorials];
        for( n=0; n<maxFactorials; ++n ) 
        {
            f1=1;
            for( l1=2; l1<n; ++l1 )
                    f1*=l1;
            factorials[n]=f1;
        }
    }
    public static int factorial(int n) 
    {
        if( n<factorials.length )
            return factorials[n];
        return -1;
    }
}