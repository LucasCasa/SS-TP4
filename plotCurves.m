function plotCurves(array)
  for i = array
    f = dlmread(fopen(["rocket" num2str(i) ".txt"]));
    plot(f(:,1),f(:,2),["o;" num2str(i) ";"]);
    hold on
  endfor  
endfunction