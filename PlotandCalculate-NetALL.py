import matplotlib.pyplot as plt

fig = plt.figure()
axis = fig.subplots(1, 4)
GA500low = {0: 5636.2725725048695, 1: 1277.475204017577, 2:3369.0000000000005, 3: 6369.000000000001, 4: 5943.000000000001, 5: 5910.000000000001, 6: 6020.000000000001, 7: 6409.000000000001, 8: 7169.000000000001, 9: 10024.0}
GA500high = {0: 10024.0, 1: 9157.0, 2: 17294.000000000004, 3: 5641.00120546273, 4: 14715.000000000004, 5: 13103.000000000004, 6: 12789.000000000004, 7: 13286.000000000004, 8: 16695.000000000004, 9: 28312.0}
GA2000low = {0: 5559.644157914909, 1: 1277.475204017577, 2: 2987.0000000000005, 3: 6053.000000000001, 4: 5522.000000000001, 5: 5460.000579842832, 6: 5594.000274662394, 7: 6028.000000000001, 8: 6580.000000000001, 9: 8438.0}
GA2000high = {9: 8438.0, 10: 7157.000000000001, 11: 15587.000000000004, 12: 1352.0017090104525, 13: 11084.000656137941, 14: 3980.0019378957804, 15: 2466.001754787518, 16: 3598.001541161211, 17: 14544.000000000004, 18: 23002.029471105525}

ACOInt500low = {0: 5256.500891724084, 1: 1161.5744087052124, 2: 1160.4974288102026, 3: 3244.3887600539056, 4: 3725.0, 5: 3900.0000000000005, 6: 5622.000000000001, 7: 5885.000000000001, 8: 6750.000000000001, 9: 8653.0}
ACOInt500high = {0: 8653.0, 1: 7908.000000000001, 2: 16833.000000000004, 3: 12546.000000000004, 4: 14572.000000000004, 5: 13010.000000000004, 6: 12758.000000000004, 7: 13286.000000000004, 8: 16313.000000000004, 9: 28312.0}
ACOInt2000low = {0: 5178.000015259022, 1: 1172.5813152938354, 2: 1010.3887652452595, 3: 3315.2061876470243, 4: 1485.9457782990512, 5: 3334.0000000000005, 6: 3271.0000000000005, 7: 5694.000000000001, 8: 6210.000000000001, 9: 7454.000000000001}
ACOInt2000high = {9: 7454.000000000001, 10: 6856.000000000001, 11: 15155.000000000004, 12: 12542.000000000004, 13: 13462.000000000004, 14: 12811.000000000004, 15: 12614.000000000004, 16: 12803.000000000004, 17: 14440.000000000004, 18: 22595.0}

IWC500low = {0: 5636.2725725048695, 1: 1277.475204017577, 2: 3369.0000000000005, 3: 6369.000000000001, 4: 5897.000000000001, 5: 3683.0000000000005, 6: 5939.000000000001, 7: 6391.000000000001, 8: 6908.000000000001, 9: 9630.0}
IWC500high = {0: 9630.0, 1: 8271.0, 2: 16614.0, 3: 1056.4456762390803, 4: 6774.420837209114, 5: 13100.000000000004, 6: 12751.000000000004, 7: 13146.000000000004, 8: 16564.000000000004, 9: 28279.0}
IWC2000low = {0: 5559.644157914909, 1: 1277.475204017577, 2: 2987.0000000000005, 3: 5777.000717174029, 4: 3894.0000000000005, 5: 5525.000000000001, 6: 5622.000000000001, 7: 6028.000000000001, 8: 6300.000000000001, 9: 8040.000000000001}
IWC2000high = {9: 8040.000000000001, 10: 6883.000000000001, 11: 15486.000000000004, 12: 1004.3341052223111, 13: 13464.000000000004, 14: 12882.000000000004, 15: 2522.0014648661017, 16: 12836.000000000004, 17: 14302.000000000004, 18: 22233.943075376785}

PSOInt500low = {0: 5178.027277775824, 1: 1072.8886207381574, 2: 1055.2144131034406, 3: 2904.25717778776, 4: 2214.59493317903, 5: 1920.7689178979886, 6: 1840.320754374898, 7: 2286.0560575255167, 8: 3420.9016838674634, 9: 6585.083513114343}
PSOInt500high = {0: 6585.083513114343, 1: 4988.0, 2: 7973.760223424324, 3: 1048.4031533399357, 4: 3798.212800009226, 5: 1732.2171162761656, 6: 1341.8336185628211, 7: 1939.6144007761002, 8: 5478.000854505226, 9: 21826.0}
PSOInt2000low = {0: 5178.026135904063, 1: 1139.32647365602, 2: 1012.2062977386718, 3: 2096.0005340657663, 4: 1408.49163116901, 5: 1370.0898489914134, 6: 1547.000289921416, 7: 2442.044362043042, 8: 3048.178215125852, 9: 4574.0}
PSOInt2000high = {9: 4574.0, 10: 4199.83679845276, 11: 7138.619127524882, 12: 1013.3269468941199, 13: 3309.1495714827415, 14: 1418.2967073817586, 15: 1232.672207408872, 16: 1477.677791167975, 17: 4462.904076947805, 18: 17975.000000000004}

WOAInt500low = {0: 5433.068867683101, 1: 1072.7851518686593, 2: 1493.0000305180438, 3: 3816.000289921416, 4: 2746.9130840696876, 5: 2258.438371816127, 6: 2083.0002288853284, 7: 5301.000167849241, 8: 4318.333410557883, 9: 7143.000701915007}
WOAInt500high = {0: 7143.000701915007, 1: 7599.167832372756, 2: 10787.098539758943, 3: 831.4215892661329, 4: 3601.666847865455, 5: 1590.555843929757, 6: 3710.001266498817, 7: 1777.5759432505827, 8: 6014.944502953586, 9: 23308.520264160365}
WOAInt2000low = {0: 5178.022999318477, 1: 1072.7851518686593, 2: 977.0616477852002, 3: 5474.0, 4: 1742.4974288102026, 5: 1215.668104914745, 6: 1502.0003051804379, 7: 1911.3152759980499, 8: 2470.102871115104, 9: 5572.000473029679}
WOAInt2000high = {9: 5572.000473029679, 10: 4697.336669767291, 11: 6001.531980702082, 12: 516.6893668577047, 13: 2707.6373176294765, 14: 1202.0842931081152, 15: 814.6903348628734, 16: 1106.4348350054818, 17: 4432.001525902189, 18: 15717.001510643167}

WOABLR500low = {0: 5178.022999318477, 1: 1077.628146441922, 2: 1005.9428654455812, 3: 2236.365688449131, 4: 1720.203868880018, 5: 1625.112838774389, 6: 1541.3897278669347, 7: 2042.0, 8: 3217.000167849241, 9: 6128.0005035477225}
WOABLR500high = {9: 6128.0005035477225, 10: 6682.000000000001, 11: 5593.37566031327, 12: 786.692387626772, 13: 2074.897065550558, 14: 1350.134109100929, 15: 1067.609432769826, 16: 1453.8157121997167, 17: 4151.9671160540165, 18: 18747.000000000004}
WOABLR2000low = {0: 5178.000015259022, 1: 1083.702506131424, 2: 792.4841326243047, 3: 1690.4913291570356, 4: 1183.4716768112844, 5: 1270.0, 6: 1376.9962919368882, 7: 1614.992305531448, 8: 2620.4440841707024, 9: 4854.271391462167}
WOABLR2000high = { 9: 4854.271391462167, 10: 3756.0000000000005, 11: 5916.85104911006, 12: 968.5600899375502, 13: 2200.7033962478417, 14: 1105.624844881234, 15: 1051.8157121997167, 16: 1247.4345406594584, 17: 4028.1977154298197, 18: 18384.000000000004}

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

fig.legend(lines, labels, loc='center', ncol=6, bbox_to_anchor=(0.5, 0.83), fontsize=13)
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
