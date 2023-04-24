import csv
import os
from pandas import read_csv
import pandas as pd

instancenum = 300
topnum = 400
directory = '...\\filtered_data_from_alibaba\\instance300.csv'
mark = 0
dfm = pd.DataFrame()
for dirpath, dirnames, files in os.walk('...\\batch_instance\\batch_instance'):
    if mark == 1:
        break
    for file in files:
        if file == 'batch_instance.csv':
            continue
        if mark == 1:
            break
        df = read_csv('...\\batch_instance\\batch_instance\\'+file,
                      names=['instance_name', 'task_name', 'job_name', 'task_type', 'status',
                             'start_time', 'end_time', 'machine_id', 'seq_no', 'total_seq_no', 'cpu_avg',
                             'cpu_max', 'mem_avg', 'mem_max'])
        df_job_task = df.drop_duplicates(subset=['task_name', 'job_name'])
        for row_label, row in df_job_task.iterrows():
            length = len(df[(df['task_name'] == row['task_name']) & (df['job_name'] == row['job_name'])].index)
            if (length > instancenum) & (length < topnum) & (row['job_name'] != 'j_59004') & (row['job_name'] != 'j_945557'):
                if len(dfm.index) == 0:
                    dfm = df[(df['task_name'] == row['task_name']) & (df['job_name'] == row['job_name'])]
                    continue
                else:
                    sum1 = dfm['end_time'] - dfm['start_time']
                df[(df['task_name'] == row['task_name']) & (df['job_name'] == row['job_name'])].to_csv(directory)
                mark = 1
                break