"""
Simple JSON Schema validator for Hima sample assets.

Usage:
  python validate_assets.py --schemas ../docs/json_schemas --assets ../docs/sample_assets

This script walks `assets_dir`, loads each JSON file, determines schema by filename prefix (letter, combined, story), and validates.
"""
import argparse
import json
import os
from jsonschema import validate, ValidationError


SCHEMA_MAP = {
    'letter': 'letter_schema.json',
    'combined': 'combined_schema.json',
    'story': 'story_page_schema.json'
}


def detect_schema(filename: str):
    name = os.path.basename(filename).lower()
    if name.startswith('letter') or name.startswith('h_letter'):
        return SCHEMA_MAP['letter']
    if name.startswith('combined'):
        return SCHEMA_MAP['combined']
    if name.startswith('story') or name.startswith('page'):
        return SCHEMA_MAP['story']
    # fallback: try to guess by content
    return None


def load_json(path: str):
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--schemas', required=True, help='Path to schemas directory')
    parser.add_argument('--assets', required=True, help='Path to assets directory')
    args = parser.parse_args()

    schemas = {}
    for fn in os.listdir(args.schemas):
        if fn.endswith('.json'):
            schemas[fn] = load_json(os.path.join(args.schemas, fn))

    errors = 0
    for fn in os.listdir(args.assets):
        if not fn.endswith('.json'):
            continue
        asset_path = os.path.join(args.assets, fn)
        schema_key = detect_schema(fn)
        if not schema_key:
            print(f"WARN: Could not detect schema for {fn}, skipping")
            continue
        schema = schemas.get(schema_key)
        if schema is None:
            print(f"ERROR: Schema {schema_key} not found")
            errors += 1
            continue
        data = load_json(asset_path)
        try:
            validate(instance=data, schema=schema)
            print(f"OK: {fn} validates against {schema_key}")
        except ValidationError as ve:
            print(f"FAIL: {fn} failed validation: {ve.message}")
            errors += 1

    if errors:
        print(f"Validation completed with {errors} error(s)")
        raise SystemExit(2)
    else:
        print("Validation completed successfully")


if __name__ == '__main__':
    main()
