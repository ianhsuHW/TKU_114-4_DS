param(
    [Parameter(Mandatory = $true)][string]$Day,
    [switch]$Run
)

# Compile each .java in <Day>\ separately.
# Class files go to bin\<Day>\<ProgramName>\ so they never overwrite each other.

$ErrorActionPreference = 'Continue'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$srcDir = Join-Path $root $Day
$binDir = Join-Path $root "bin\$Day"

if (-not (Test-Path $srcDir)) { Write-Host "source folder not found: $srcDir"; exit 1 }

$failed = @()
$sources = Get-ChildItem -Path $srcDir -Filter *.java | Sort-Object Name

foreach ($source in $sources) {
    $name = [System.IO.Path]::GetFileNameWithoutExtension($source.Name)
    $out = Join-Path $binDir $name
    New-Item -ItemType Directory -Force -Path $out | Out-Null

    $compile = & javac -encoding UTF-8 -d $out $source.FullName 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "COMPILE FAIL  $name"
        $compile | ForEach-Object { Write-Host "    $_" }
        $failed += $name
        continue
    }

    if ($Run) {
        Write-Host "===== $name ====="
        & java -cp $out $name
        if ($LASTEXITCODE -ne 0) {
            Write-Host "RUN FAIL  $name"
            $failed += $name
        }
    } else {
        Write-Host "OK  $name"
    }
}

Write-Host ""
Write-Host "total=$($sources.Count)  failed=$($failed.Count)"
if ($failed.Count -gt 0) { $failed | ForEach-Object { Write-Host "  FAILED: $_" } }
