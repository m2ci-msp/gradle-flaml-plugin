form Generate test resources
  sentence Output_directory
endform

snd = Create Sound from formula... sineWithNoise 1 0 1 44100 1/2 * sin(2*pi*377*x) + randomGauss(0,0.1)
Multiply by window... Hanning
Save as WAV file... 'output_directory$'/foobar.wav
Save as FLAC file... 'output_directory$'/foobar.flac

Extract part... 0 0.5 rectangular 1 no
Save as WAV file... 'output_directory$'/foo_padded.wav
select snd
Extract part... 0.1 0.4 rectangular 1 no
Save as WAV file... 'output_directory$'/foo.wav
select snd
Extract part... 0.5 1 rectangular 1 no
Save as WAV file... 'output_directory$'/bar_padded.wav
select snd
Extract part... 0.6 0.9 rectangular 1 no
Save as WAV file... 'output_directory$'/bar.wav
select snd

tg = To TextGrid... prompts

Insert boundary... 1 0.1
Insert boundary... 1 0.4
Insert boundary... 1 0.6
Insert boundary... 1 0.9
Duplicate tier... 1 2 segments
Insert boundary... 2 0.2
Insert boundary... 2 0.7
Insert boundary... 2 0.8

Set interval text... 1 2 foo
Set interval text... 1 4 bar
Set interval text... 2 2 f
Set interval text... 2 3 u
Set interval text... 2 5 b
Set interval text... 2 6 a
Set interval text... 2 7 r

Save as text file... 'output_directory$'/foobar.TextGrid

Extract part... 0.1 0.4 no
Extract tier... 2
Save as Xwaves label file... 'output_directory$'/foo.lab
select tg
Extract part... 0.6 0.9 no
Extract tier... 2
Save as Xwaves label file... 'output_directory$'/bar.lab

foo$ = "Foo."
foo$ > 'output_directory$'/foo.txt
bar$ = "Bar."
bar$ > 'output_directory$'/bar.txt
