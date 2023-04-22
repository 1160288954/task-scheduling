import matplotlib.pyplot as plt

fig = plt.figure()
axis = fig.subplots(1, 4)
GA500low = {0: 3680.0, 1: 539.0, 2: 1035.0, 3: 2404.0, 4: 1976.0, 5: 1701.0, 6: 1758.0, 7: 2273.0, 8: 3454.0, 9: 6904.0}
GA500high = {9: 6904.0, 10: 4802.0, 11: 6633.0, 12: 275.0, 13: 3190.0, 14: 879.0, 15: 760.0, 16: 1344.0, 17: 5112.0, 18: 18635.0}
GA2000low = {0: 3680.0, 1: 539.0, 2: 750.0, 3: 1892.0, 4: 1608.0, 5: 1466.0, 6: 1424.0, 7: 1946.0, 8: 3091.0, 9: 6244.0}
GA2000high = {9: 6244.0, 10: 3954.0, 11: 5933.0, 12: 269.0, 13: 2684.0, 14: 825.0, 15: 661.0, 16: 1078.0, 17: 4874.0, 18: 18131.0}

ACOInt500low = {0: 3680.0, 1: 539.0, 2: 854.0, 3: 2037.0, 4: 1666.0, 5: 1433.0, 6: 1549.0, 7: 1882.0, 8: 2877.0, 9: 5858.0}
ACOInt500high = { 9: 5858.0, 10: 4509.0, 11: 6101.0, 12: 264.0, 13: 2944.0, 14: 786.0, 15: 697.0, 16: 1182.0, 17: 4775.0, 18: 18635.0}
ACOInt2000low = {0: 3680.0, 1: 539.0, 2: 522.0, 3: 1422.0, 4: 1140.0, 5: 1167.0, 6: 1169.0, 7: 1469.0, 8: 2342.0, 9: 4554.0}
ACOInt2000high = {9: 4554.0, 10: 3660.0, 11: 5663.0, 12: 215.0, 13: 2612.0, 14: 765.0, 15: 646.0, 16: 949.0, 17: 4397.0, 18: 16909.0}

IWC500low = {0: 3680.0, 1: 539.0, 2: 931.0, 3: 2404.0, 4: 1738.0, 5: 1701.0, 6: 1615.0, 7: 2008.0, 8: 3440.0, 9: 6514.0}
IWC500high = {9: 6514.0, 10: 4515.0, 11: 6463.0, 12: 268.0, 13: 3142.0, 14: 835.0, 15: 754.0, 16: 1239.0, 17: 4876.0, 18: 18635.0}
IWC2000low = {0: 3680.0, 1: 539.0, 2: 743.0, 3: 1814.0, 4: 1371.0, 5: 1318.0, 6: 1351.0, 7: 1648.0, 8: 3091.0, 9: 5574.0}
IWC2000high = {9: 5574.0, 10: 3812.0, 11: 5549.0, 12: 260.0, 13: 2643.0, 14: 825.0, 15: 661.0, 16: 1053.0, 17: 4663.0, 18: 17632.0}

PSOInt500low = {0: 3680.0, 1: 539.0, 2: 879.0, 3: 2004.0, 4: 1699.0, 5: 1463.0, 6: 1533.0, 7: 1837.0, 8: 2958.0, 9: 5361.0}
PSOInt500high = { 9: 5361.0, 10: 4375.0, 11: 6019.0, 12: 248.0, 13: 2650.0, 14: 753.0, 15: 612.0, 16: 1144.0, 17: 4146.0, 18: 16177.0}
PSOInt2000low = {0: 3680.0, 1: 539.0, 2: 490.0, 3: 1327.0, 4: 866.0, 5: 1187.0, 6: 954.0, 7: 1234.0, 8: 1923.0, 9: 4106.0}
PSOInt2000high = {9: 4106.0, 10: 2775.0, 11: 4834.0, 12: 196.0, 13: 1722.0, 14: 656.0, 15: 469.0, 16: 686.0, 17: 2643.0, 18: 11572.0}

WOAInt500low = {0: 3680.0, 1: 539.0, 2: 935.0, 3: 2451.0, 4: 1966.0, 5: 1620.0, 6: 1657.0, 7: 2209.0, 8: 3141.0, 9: 6060.0}
WOAInt500high = { 9: 6060.0, 10: 5091.0, 11: 6367.0, 12: 273.0, 13: 3033.0, 14: 844.0, 15: 735.0, 16: 1251.0, 17: 4759.0, 18: 18949.0}
WOAInt2000low = {0: 3680.0, 1: 539.0, 2: 604.0, 3: 1304.0, 4: 1437.0, 5: 974.0, 6: 1129.0, 7: 1415.0, 8: 2607.0, 9: 3814.0}
WOAInt2000high = {9: 3814.0, 10: 3461.0, 11: 5695.0, 12: 214.0, 13: 1957.0, 14: 680.0, 15: 585.0, 16: 681.0, 17: 3324.0, 18: 12954.0}

WOABLR500low = {0: 3680.0, 1: 539.0, 2: 699.0, 3: 1615.0, 4: 1381.0, 5: 699.0, 6: 1313.0, 7: 1676.0, 8: 2583.0, 9: 4968.0}
WOABLR500high = { 9: 4968.0, 10: 3837.0, 11: 5138.0, 12: 216.0, 13: 2458.0, 14: 791.0, 15: 561.0, 16: 1004.0, 17: 3637.0, 18: 14709.0}
WOABLR2000low = {0: 3680.0, 1: 539.0, 2: 452.0, 3: 1233.0, 4: 1092.0, 5: 897.0, 6: 937.0, 7: 1269.0, 8: 1906.0, 9: 3605.0}
WOABLR2000high = {9: 3605.0, 10: 3162.0, 11: 3976.0, 12: 206.0, 13: 1724.0, 14: 653.0, 15: 352.0, 16: 731.0, 17: 2601.0, 18: 12109.0}

ga500low = list(GA500low.values())
acoInt500low = list(ACOInt500low.values())
iwc500low = list(IWC500low.values())
psoInt500low = list(PSOInt500low.values())
woaInt500low = list(WOAInt500low.values())
woablr500low = list(WOABLR500low.values())

ga500high = list(GA500high.values())
acoInt500high = list(ACOInt500high.values())
iwc500high = list(IWC500high.values())
psoInt500high = list(PSOInt500high.values())
woaInt500high = list(WOAInt500high.values())
woablr500high = list(WOABLR500high.values())

ga2000low = list(GA2000low.values())
acoInt2000low = list(ACOInt2000low.values())
iwc2000low = list(IWC2000low.values())
psoInt2000low = list(PSOInt2000low.values())
woaInt2000low = list(WOAInt2000low.values())
woablr2000low = list(WOABLR2000low.values())

ga2000high = list(GA2000high.values())
acoInt2000high = list(ACOInt2000high.values())
iwc2000high = list(IWC2000high.values())
psoInt2000high = list(PSOInt2000high.values())
woaInt2000high = list(WOAInt2000high.values())
woablr2000high = list(WOABLR2000high.values())

xl = [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
xh = [1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]

axis[0].plot(xl, ga500low, linestyle="--", marker=",", linewidth='1', label="ga")
axis[0].plot(xl, acoInt500low, linestyle="--", marker="+", linewidth='1', label="aco")
axis[0].plot(xl, iwc500low, linestyle="-.", marker="x", linewidth='1', label="iwc")
axis[0].plot(xl, psoInt500low, linestyle="-", marker="v", linewidth='1', label="pso")
axis[0].plot(xl, woaInt500low, linestyle="-.", marker="*", linewidth='1', label="woa")
axis[0].plot(xl, woablr500low, linestyle="-", marker="o", linewidth='1', label="ins")
axis[0].set_title("a. busy period; short jobs")

axis[1].plot(xh, ga500high, linestyle="--", marker=",", linewidth='1')
axis[1].plot(xh, acoInt500high, linestyle="--", marker="+", linewidth='1')
axis[1].plot(xh, iwc500high, linestyle="-.", marker="x", linewidth='1')
axis[1].plot(xh, psoInt500high, linestyle="-", marker="v", linewidth='1')
axis[1].plot(xh, woaInt500high, linestyle="-.", marker="*", linewidth='1')
axis[1].plot(xh, woablr500high, linestyle="-", marker="o", linewidth='1')
axis[1].set_title("b. busy period; long jobs")

axis[2].plot(xl, ga2000low, linestyle="--", marker=",", linewidth='1')
axis[2].plot(xl, acoInt2000low, linestyle="--", marker="+", linewidth='1')
axis[2].plot(xl, iwc2000low, linestyle="-.", marker="x", linewidth='1')
axis[2].plot(xl, psoInt2000low, linestyle="-", marker="v", linewidth='1')
axis[2].plot(xl, woaInt2000low, linestyle="-.", marker="*", linewidth='1')
axis[2].plot(xl, woablr2000low, linestyle="-", marker="o", linewidth='1')
axis[2].set_title("c. idle period; short jobs")

axis[3].plot(xh, ga2000high, linestyle="--", marker=",", linewidth='1')
axis[3].plot(xh, acoInt2000high, linestyle="--", marker="+", linewidth='1')
axis[3].plot(xh, iwc2000high, linestyle="-.", marker="x", linewidth='1')
axis[3].plot(xh, psoInt2000high, linestyle="-", marker="v", linewidth='1')
axis[3].plot(xh, woaInt2000high, linestyle="-.", marker="*", linewidth='1')
axis[3].plot(xh, woablr2000high, linestyle="-", marker="o", linewidth='1')
axis[3].set_title("d. idle period; long jobs")

lines = []
labels = []
for ax in fig.axes:
    Line, Label = ax.get_legend_handles_labels()
    lines.extend(Line)
    labels.extend(Label)

#fig.legend(lines, labels, loc='center', ncol=6, bbox_to_anchor=(0.5, 0.93))
plt.show()


var = []
max = 0
for i in range(10):
    percent = (ga500low[i] - woablr500low[i]) / woablr500low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("GA500low: ", max)

var = []
max = 0
for i in range(10):
    percent = (ga500high[i] - woablr500high[i]) / woablr500high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("GA500high: ", max)

var = []
max = 0
for i in range(10):
    percent = (acoInt500low[i] - woablr500low[i]) / woablr500low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("acoInt500low: ", max)

var = []
max = 0
for i in range(10):
    percent = (acoInt500high[i] - woablr500high[i]) / woablr500high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("acoInt500high: ", max)

var = []
max = 0
for i in range(10):
    percent = (iwc500low[i] - woablr500low[i]) / woablr500low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("iwc500low: ", max)

var = []
max = 0
for i in range(10):
    percent = (iwc500high[i] - woablr500high[i]) / woablr500high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("iwc500high: ", max)

var = []
max = 0
for i in range(10):
    percent = (psoInt500low[i] - woablr500low[i]) / woablr500low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("psoInt500low: ", max)

var = []
max = 0
for i in range(10):
    percent = (psoInt500high[i] - woablr500high[i]) / woablr500high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("psoInt500high: ", max)

var = []
max = 0
for i in range(10):
    percent = (woaInt500low[i] - woablr500low[i]) / woablr500low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("woaInt500low: ", max)

var = []
max = 0
for i in range(10):
    percent = (woaInt500high[i] - woablr500high[i]) / woablr500high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("woaInt500high: ", max)



var = []
max = 0
for i in range(10):
    percent = (ga2000low[i] - woablr2000low[i]) / woablr2000low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("GA2000low: ", max)

var = []
max = 0
for i in range(10):
    percent = (ga2000high[i] - woablr2000high[i]) / woablr2000high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("GA2000high: ", max)

var = []
max = 0
for i in range(10):
    percent = (acoInt2000low[i] - woablr2000low[i]) / woablr2000low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("acoInt2000low: ", max)

var = []
max = 0
for i in range(10):
    percent = (acoInt2000high[i] - woablr2000high[i]) / woablr2000high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("acoInt2000high: ", max)

var = []
max = 0
for i in range(10):
    percent = (iwc2000low[i] - woablr2000low[i]) / woablr2000low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("iwc2000low: ", max)

var = []
max = 0
for i in range(10):
    percent = (iwc2000high[i] - woablr2000high[i]) / woablr2000high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("iwc2000high: ", max)

var = []
max = 0
for i in range(10):
    percent = (psoInt2000low[i] - woablr2000low[i]) / woablr2000low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("psoInt2000low: ", max)

var = []
max = 0
for i in range(10):
    percent = (psoInt2000high[i] - woablr2000high[i]) / woablr2000high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("psoInt2000high: ", max)

var = []
max = 0
for i in range(10):
    percent = (woaInt2000low[i] - woablr2000low[i]) / woablr2000low[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("woaInt2000low: ", max)

var = []
max = 0
for i in range(10):
    percent = (woaInt2000high[i] - woablr2000high[i]) / woablr2000high[i]
    var.append(percent)
for i in range(10):
    if var[i] > max:
        max = var[i]
print("woaInt2000high: ", max)
