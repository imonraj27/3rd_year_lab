#include <iostream>
#include <cmath>
#include <cstdlib>
#include <ctime>

using namespace std;

double f(double x)
{
    // This is the function we want to find the maximum value of
    return -x * sin(sqrt(abs(x)));
}

double getNeighbor(double x, double range)
{
    // This function returns a random neighbor of the given x value
    double y = x + (range * (2 * rand() / (double)RAND_MAX - 1));
    return y;
}

double simulatedAnnealing(double x, double range, double temperature, double coolingRate, int iterationsPerTemperature)
{
    double currentX = x;
    double currentScore = f(currentX);
    double bestX = currentX;
    double bestScore = currentScore;

    while (temperature > 1e-10)
    {
        for (int i = 0; i < iterationsPerTemperature; i++)
        {
            double neighborX = getNeighbor(currentX, range);
            double neighborScore = f(neighborX);
            double delta = neighborScore - currentScore;

            if (delta > 0 || exp(delta / temperature) > (double)rand() / RAND_MAX)
            {
                currentX = neighborX;
                currentScore = neighborScore;
                cout << "X: " << currentX << ", f(X): " << currentScore << endl;
                if (currentScore > bestScore)
                {
                    bestX = currentX;
                    bestScore = currentScore;
                }
            }
        }

        temperature *= coolingRate;
    }

    return bestX;
}

int main()
{
    srand(time(NULL));

    double initialX = 1.0;              // starting value of x
    double range = 10.0;                // range of values to consider for neighbors
    double temperature = 100.0;         // starting temperature
    double coolingRate = 0.95;          // cooling rate for temperature
    int iterationsPerTemperature = 100; // number of iterations per temperature

    double maxX = simulatedAnnealing(initialX, range, temperature, coolingRate, iterationsPerTemperature);

    cout << "Maximum value of f(x) found at x = " << maxX << endl;

    return 0;
}
