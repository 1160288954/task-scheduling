#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import matplotlib.pyplot as plt

GA = {0: 3680.0, 1: 539.0, 2: 1035.0, 3: 2404.0, 4: 1976.0, 5: 1701.0, 6: 1758.0, 7: 2273.0,
      8: 3454.0, 9: 6904.0}

ACOInt = {0: 3680.0, 1: 539.0, 2: 854.0, 3: 2037.0, 4: 1666.0, 5: 1433.0, 6: 1549.0, 7: 1882.0,
          8: 2877.0, 9: 5858.0}

IWC = {0: 3680.0, 1: 539.0, 2: 931.0, 3: 2404.0, 4: 1738.0, 5: 1701.0, 6: 1615.0, 7: 2008.0,
       8: 3440.0, 9: 6514.0}

PSOInt = {0: 3680.0, 1: 539.0, 2: 879.0, 3: 2004.0, 4: 1699.0, 5: 1463.0, 6: 1533.0, 7: 1837.0,
          8: 2958.0, 9: 5361.0}

WOAInt = {0: 3680.0, 1: 539.0, 2: 935.0, 3: 2451.0, 4: 1966.0, 5: 1620.0, 6: 1657.0, 7: 2209.0,
          8: 3141.0, 9: 6060.0}

WOABLR = {0: 3680.0, 1: 539.0, 2: 699.0, 3: 1615.0, 4: 1381.0, 5: 699.0, 6: 1313.0, 7: 1676.0,
          8: 2583.0, 9: 4968.0}

ga = list(GA.values())
acoInt = list(ACOInt.values())
iwc = list(IWC.values())
psoInt = list(PSOInt.values())
woaInt = list(WOAInt.values())
woablr = list(WOABLR.values())
x = [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
plt.plot(x, ga, linestyle="--", marker=",", linewidth='1', label="GA")
plt.plot(x, acoInt, linestyle="--", marker="+", linewidth='1', label="ACO")
plt.plot(x, iwc, linestyle="-.", marker="x", linewidth='1', label="IWC")
plt.plot(x, psoInt, linestyle="-", marker="v", linewidth='1', label="PSO")

plt.plot(x, woaInt, linestyle="-.", marker="*", linewidth='1', label="WOA")
plt.plot(x, woablr, linestyle="-", marker="o", linewidth='1', label="RTGS")

plt.xlabel('task number', fontsize=12)
plt.ylabel('final finish time gap', fontsize=12)
plt.legend(ncol=3, fontsize=10)
plt.savefig("50-svmad-svmm-svm-df-blr.jpg")
plt.show()
