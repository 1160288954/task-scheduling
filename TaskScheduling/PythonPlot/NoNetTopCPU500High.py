#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import matplotlib.pyplot as plt

GA = {9: 6904.0, 10: 4802.0, 11: 6633.0, 12: 275.0, 13: 3190.0, 14: 879.0, 15: 760.0,
      16: 1344.0, 17: 5112.0, 18: 18635.0}

ACOInt = { 9: 5858.0, 10: 4509.0, 11: 6101.0, 12: 264.0, 13: 2944.0, 14: 786.0,
          15: 697.0, 16: 1182.0, 17: 4775.0, 18: 18635.0}

IWC = {9: 6514.0, 10: 4515.0, 11: 6463.0, 12: 268.0, 13: 3142.0, 14: 835.0, 15: 754.0,
       16: 1239.0, 17: 4876.0, 18: 18635.0}

PSOInt = { 9: 5361.0, 10: 4375.0, 11: 6019.0, 12: 248.0, 13: 2650.0, 14: 753.0,
          15: 612.0, 16: 1144.0, 17: 4146.0, 18: 16177.0}

WOAInt = { 9: 6060.0, 10: 5091.0, 11: 6367.0, 12: 273.0, 13: 3033.0, 14: 844.0,
          15: 735.0, 16: 1251.0, 17: 4759.0, 18: 18949.0}

WOABLR = { 9: 4968.0, 10: 3837.0, 11: 5138.0, 12: 216.0, 13: 2458.0, 14: 791.0,
          15: 561.0, 16: 1004.0, 17: 3637.0, 18: 14709.0}

ga = list(GA.values())
acoInt = list(ACOInt.values())
iwc = list(IWC.values())
psoInt = list(PSOInt.values())
woaInt = list(WOAInt.values())
woablr = list(WOABLR.values())

x = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]

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
