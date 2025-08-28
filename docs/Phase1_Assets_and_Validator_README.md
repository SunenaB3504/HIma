# Phase 1 — Asset Pipeline & Validator

Date: August 28, 2025

This folder contains JSON Schemas, sample assets, and a small Python validator for Phase 1 of Hima (Asset Pipeline & Minimal Content).

Files added
- `docs/json_schemas/letter_schema.json` — schema for letter JSON
- `docs/json_schemas/combined_schema.json` — schema for combined consonant+vowel entries
- `docs/json_schemas/story_page_schema.json` — schema for story page JSON
- `docs/sample_assets/` — sample JSON files: one letter, one combined sound, one story page
- `tools/validate_assets/validate_assets.py` — small CLI validator
- `tools/validate_assets/requirements.txt` — Python requirements for validator

How to run the validator (Windows PowerShell)

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r tools\validate_assets\requirements.txt
python tools\validate_assets\validate_assets.py --schemas docs\json_schemas --assets docs\sample_assets
```

Expected outcome: the validator prints OK for each sample JSON and exits successfully.

Notes
- The validator currently detects schema by filename prefix. Expand detection rules if you use different filenames.
- After content preprocessing, run the validator in CI to reject malformed assets before packaging.

End of README
