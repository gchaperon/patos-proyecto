import csv

with open("root_all_wrepeat_tsdate.tsv", "r") as f_in:
  with open("root_all_wrepeat_tsdate_fixed.tsv", "w") as f_out:
    reader = csv.reader(f_in, delimiter='\t')
    writer = csv.writer(f_out, delimiter='\t')
    for i, row in enumerate(reader):
      if i%10000 is 0:
        print(f"Reading root row {i}")
      writer.writerow(" ".join(col.split()) for col in row)

with open("child_all_wrepeat_tsdate.tsv", "r") as f_in:
  with open("child_all_wrepeat_tsdate_fixed.tsv", "w") as f_out:
    reader = csv.reader(f_in, delimiter='\t')
    writer = csv.writer(f_out, delimiter='\t')
    for i, row in enumerate(reader):
      if i%10000 is 0:
        print(f"Reading child row {i}")
      writer.writerow(" ".join(col.split()) for col in row)

