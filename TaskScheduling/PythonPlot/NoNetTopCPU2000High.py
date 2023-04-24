#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import matplotlib.pyplot as plt

GA = {9: 6244.0, 10: 3954.0, 11: 5933.0, 12: 269.0, 13: 2684.0, 14: 825.0, 15: 661.0, 16: 1078.0, 17: 4874.0, 18: 18131.0}

ACOInt = {9: 4554.0, 10: 3660.0, 11: 5663.0, 12: 215.0, 13: 2612.0, 14: 765.0, 15: 646.0, 16: 949.0, 17: 4397.0, 18: 16909.0}

IWC = {9: 5574.0, 10: 3812.0, 11: 5549.0, 12: 260.0, 13: 2643.0, 14: 825.0, 15: 661.0, 16: 1053.0, 17: 4663.0, 18: 17632.0}

PSOInt = {9: 4106.0, 10: 2775.0, 11: 4834.0, 12: 196.0, 13: 1722.0, 14: 656.0, 15: 469.0, 16: 686.0, 17: 2643.0, 18: 11572.0}

WOAInt = {9: 3814.0, 10: 3461.0, 11: 5695.0, 12: 214.0, 13: 1957.0, 14: 680.0, 15: 585.0, 16: 681.0, 17: 3324.0, 18: 12954.0}

WOABLR = {9: 3605.0, 10: 3162.0, 11: 3976.0, 12: 206.0, 13: 1724.0, 14: 653.0, 15: 352.0, 16: 731.0, 17: 2601.0, 18: 12109.0}

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
