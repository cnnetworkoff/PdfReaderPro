#!/usr/bin/env bash
set -euo pipefail

ROOT="/home/ubuntu/PdfReaderPro"
SRC_DIR="$ROOT/ss"
OUT_DIR="$ROOT/ss/playstore-featured-v3"
TMP_DIR="$OUT_DIR/.tmp"

mkdir -p "$OUT_DIR" "$TMP_DIR"
rm -f "$OUT_DIR"/*.png
rm -f "$TMP_DIR"/*

CANVAS_W=1440
CANVAS_H=3120
PHONE_W=1040
PHONE_H=2060
SCREEN_W=906
SCREEN_H=1768
SCREEN_INSET_X=67
SCREEN_INSET_Y=122
STATUS_CROP_Y=124
STATUS_CROP_H=1326
PHONE_X=200
PHONE_Y=930
SCREEN_X=$((PHONE_X + SCREEN_INSET_X))
SCREEN_Y=$((PHONE_Y + SCREEN_INSET_Y))

TITLE_FONT="DejaVu-Sans-Bold"
BODY_FONT="DejaVu-Sans"
INK="#ffffff"
SUB="#ffd9d9"
CHIP="#ffffff"
CHIP_TEXT="#d31616"
RED="#ff2b2b"
RED_DEEP="#a40000"
GLOW='rgba(255,66,66,0.24)'

build_one() {
  local src="$1"
  local out="$2"
  local title="$3"
  local subtitle="$4"

  local base="$TMP_DIR/${out%.png}"
  local shot="$base-shot.png"
  local shot_round="$base-shot-round.png"
  local mask="$base-mask.png"
  local shadow="$base-shadow.png"
  local phone="$base-phone.png"
  local canvas="$base-canvas.png"
  local final="$OUT_DIR/$out"

  convert "$src" \
    -crop "720x${STATUS_CROP_H}+0+${STATUS_CROP_Y}" +repage \
    -resize "${SCREEN_W}x${SCREEN_H}^" \
    -gravity center -crop "${SCREEN_W}x${SCREEN_H}+0+0" +repage \
    "$shot"

  convert -size "${SCREEN_W}x${SCREEN_H}" xc:none \
    -fill white \
    -draw "roundrectangle 0,0,$((SCREEN_W-1)),$((SCREEN_H-1)),94,94" \
    "$mask"

  convert "$shot" "$mask" -compose CopyOpacity -composite "$shot_round"

  convert -size "${PHONE_W}x${PHONE_H}" xc:none \
    -fill 'rgba(0,0,0,0.38)' \
    -draw "roundrectangle 44,50,$((PHONE_W-26)),$((PHONE_H-16)),168,168" \
    -blur 0x36 \
    "$shadow"

  convert -size "${PHONE_W}x${PHONE_H}" xc:none \
    -fill '#f6f6f8' \
    -draw "roundrectangle 0,0,$((PHONE_W-1)),$((PHONE_H-1)),170,170" \
    -fill '#d9d9de' \
    -draw "roundrectangle 10,10,$((PHONE_W-11)),$((PHONE_H-11)),164,164" \
    -fill '#080809' \
    -draw "roundrectangle 28,28,$((PHONE_W-29)),$((PHONE_H-29)),154,154" \
    -fill '#1a1a1e' \
    -draw "roundrectangle 388,48,652,106,30,30" \
    -fill '#2d2d31' \
    -draw "roundrectangle 414,64,626,90,18,18" \
    "$phone"

  convert -size "${CANVAS_W}x${CANVAS_H}" "gradient:#540000-#ff2929" \
    -fill 'rgba(255,255,255,0.06)' \
    -draw "roundrectangle 54,54,$((CANVAS_W-55)),$((CANVAS_H-55)),64,64" \
    -fill 'rgba(255,255,255,0.08)' \
    -draw "circle 1260,250 1600,250" \
    -fill 'rgba(255,255,255,0.05)' \
    -draw "circle 120,2860 620,2860" \
    -fill "$GLOW" \
    -draw "circle 1120,1080 1440,1080" \
    "$canvas"

  convert "$canvas" \
    -fill "$CHIP" -draw "roundrectangle 112,130,462,212,40,40" \
    -fill "$CHIP_TEXT" -font "$BODY_FONT" -pointsize 40 -gravity northwest \
    -annotate +150+149 "PDF Reader Pro" \
    -fill "$INK" -font "$TITLE_FONT" -pointsize 124 -gravity northwest \
    -annotate +106+308 "$title" \
    -fill "$SUB" -font "$BODY_FONT" -pointsize 48 -gravity northwest \
    -annotate +114+692 "$subtitle" \
    "$shadow" -geometry "+$((PHONE_X+24))+$((PHONE_Y+34))" -composite \
    "$phone" -geometry "+${PHONE_X}+${PHONE_Y}" -composite \
    "$shot_round" -geometry "+${SCREEN_X}+${SCREEN_Y}" -composite \
    "$final"
}

build_one "$SRC_DIR/Screenshot_20260415-121348.png" "01-welcome-to-faster-reading.png" "Welcome to\nfaster reading" "Launch into a polished PDF experience with\nquick access and clean onboarding."
build_one "$SRC_DIR/Screenshot_20260415-121354.png" "02-built-for-daily-document-work.png" "Built for daily\ndocument work" "A focused interface keeps your most useful\nreader actions close at hand."
build_one "$SRC_DIR/Screenshot_20260415-121500.png" "03-manage-your-pdf-library.png" "Manage your\nPDF library" "Browse recent files, favorites and your full\ncollection from one place."
build_one "$SRC_DIR/Screenshot_20260415-121514.png" "04-explore-folders-with-ease.png" "Explore folders\nwith ease" "Navigate local storage faster with a simple\nfolder view and document grouping."
build_one "$SRC_DIR/Screenshot_20260415-121524.png" "05-core-pdf-tools-ready.png" "Core PDF tools\nready" "Merge, split, compress and organize pages\nwithout extra apps."
build_one "$SRC_DIR/Screenshot_20260415-121532.png" "06-advanced-tools-when-needed.png" "Advanced tools\nwhen needed" "Unlock files, add watermarks and convert\ncontent with extra utilities."
build_one "$SRC_DIR/Screenshot_20260415-122112.png" "07-read-pages-with-focus.png" "Read pages\nwith focus" "A large clean canvas helps you stay on the\ndocument instead of the chrome."
build_one "$SRC_DIR/Screenshot_20260415-122124.png" "08-highlight-draw-and-edit.png" "Highlight, draw\nand edit" "Open editing controls for notes, drawing,\nstamps and markup."
build_one "$SRC_DIR/Screenshot_20260415-122142.png" "09-use-smart-reading-controls.png" "Use smart\nreading controls" "Auto-scroll and reader options make long\nreading sessions more comfortable."
build_one "$SRC_DIR/Screenshot_20260415-122219.png" "10-check-file-details-fast.png" "Check file\ndetails fast" "See metadata, information and file actions\nwithout leaving the document flow."

montage "$OUT_DIR"/*.png -geometry 200x434+14+14 -tile 2x5 "$OUT_DIR/contact-sheet-featured-v3.png"
